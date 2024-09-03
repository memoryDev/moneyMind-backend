package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.dto.CustomUserDetails;
import dev.momory.moneymindbackend.entity.User;
import dev.momory.moneymindbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

        log.info("CustomUserDetailsService loadUserByUserid = {}", userid);

        // 로그인 userid 존재 하는지 확인
//        Boolean isExist = userRepository.existsByUserid(userid);
//        log.info("isExist = {}", isExist);

        User user = userRepository.findByUserid(userid);
        log.info("userid={}", user.getUserid());
        log.info("email={}", user.getEmail());
        log.info("password={}", user.getPassword());
        log.info("username={}", user.getUsername());
        log.info("provider={}", user.getAuthProvider().name());

        if (user != null) {
            return new CustomUserDetails(user);
        }

        return null;
    }
}
