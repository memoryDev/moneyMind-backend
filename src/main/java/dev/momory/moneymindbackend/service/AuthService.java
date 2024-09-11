package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.dto.SignUpRequest;
import dev.momory.moneymindbackend.entity.User;
import dev.momory.moneymindbackend.repository.UserRepository;
import dev.momory.moneymindbackend.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final RedisUtil redisUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    public void logout(String token, RedirectAttributes redirectAttributes) {
        // token 블랙리스트에 추가
        // 1시간동안 블랙리스트에 유지
        redisTemplate.opsForValue().set("blacklist:" + token, "true", Duration.ofHours(1));

        // 쿠키에서 Refresh Token 삭제
        redirectAttributes.addFlashAttribute("logoutSuccess", true);
    }

    public Boolean checkUseridDuplicate(String userid) {
        return userRepository.existsByUserid(userid);
    }

    public User signupUser (SignUpRequest signUpRequest) {

        // DTO -> Entity 변환
        User entity = signUpRequest.toEntity();

        //password 암호화
        entity.encryptPassword(encoder.encode(signUpRequest.getPassword()));
        log.debug("AuthService.signupUser - entity = {}", entity);

        // entity값이 null이거나 빈 값이면 null 반환
        if (ObjectUtils.isEmpty(entity)) {
            log.debug("AuthService.entity.isEmpty = {}", "true");
            throw new IllegalArgumentException("유효하지 않은 사용자 정보입니다.");
        }

        // 아이디 중복 체크
        Boolean isUserExists = userRepository.existsByUserid(entity.getUserid());
        if (isUserExists) {
            log.error("AuthService.signupUser: 이미 존재하는 아이디입니다. {}", entity.getUserid());
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 사용자 정보 저장
        User savedUser = userRepository.save(entity);
        log.info("AuthService.signupUser - 사용자 저장 성공 = {}", savedUser);

        return savedUser;
    }
}
