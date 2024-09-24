package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.dto.SignUpRequest;
import dev.momory.moneymindbackend.entity.User;
import dev.momory.moneymindbackend.exception.CustomException;
import dev.momory.moneymindbackend.repository.UserRepository;
import dev.momory.moneymindbackend.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
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

        Boolean isUserExists = userRepository.existsByUserid(userid);

        // 가입여부 체크
        if (isUserExists) {
            log.warn("AuthController.checkUseridDuplicate = {} - DUPLICATE_USER_ID", userid);
            throw new CustomException(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.", "DUPLICATE_USER_ID");
        }

        return isUserExists;
    }

    public User signupUser (SignUpRequest signUpRequest) {

        // DTO -> Entity 변환
        User entity = signUpRequest.toEntity();

        //password 암호화
        entity.encryptPassword(encoder.encode(signUpRequest.getPassword()));
        log.debug("AuthService.signupUser - entity = {}", entity);

        // entity값이 null이거나 빈 값이면 null 반환
        if (ObjectUtils.isEmpty(entity)) {
            log.debug("AuthService.entity.isEmpty = {}", "ENTITY_FIELD_REQUIRED");
            throw new CustomException(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 정보입니다.", "ENTITY_FIELD_REQUIRED");
        }

        // 아이디 중복 체크
        Boolean isUserExists = checkUseridDuplicate(entity.getUserid());

        if (isUserExists) {
            log.error("AuthService.signupUser: 이미 존재하는 아이디입니다. - {}", entity.getUserid());
            throw new CustomException(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.", "DUPLICATE_USER_ID");
        }

        // 사용자 정보 저장
        User savedUser = userRepository.save(entity);
        log.info("AuthService.signupUser - 사용자 저장 성공 = {}", savedUser);

        return savedUser;
    }
}
