package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.dto.LogoutRequest;
import dev.momory.moneymindbackend.dto.TokenCategory;
import dev.momory.moneymindbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 로그아웃 요청을 처리하는 메서드
     * 사용자가 로그아웃을 요청할때, 로그아웃 처리를 하고 리프레시 토큰을 쿠키에서 제거함
     * @param logoutRequest 로그아웃 요청 정보(Access Token 포함)
     * @param redirectAttributes 리다이렉트 시 사용할 속성
     * @return 로그아웃 성공 메세지를 포함한 HTTP 응답
     */
    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(
            @RequestBody LogoutRequest logoutRequest,
            RedirectAttributes redirectAttributes) {

        log.debug("AuthController.logout = {}", logoutRequest.getAccess());

        // 서비스 로직
        authService.logout(logoutRequest.getAccess(), redirectAttributes);

        // Refresh Token 쿠키에서 제거하기 위한 쿠키 설정
        ResponseCookie cookie = ResponseCookie.from(TokenCategory.REFRESH.getValue(), "")
                .httpOnly(true) // HttpOnly설정: 클라이언트 측에서 스크립트를 통해 접근X
                .secure(false) // 쿠키의 경로 설정에서만 쿠키 전송 여부 설정
                .path("/") // 쿠키의 경로 설정
                .maxAge(0) // 쿠키의 만료시간 설정(즉시만료)
                .build(); // 쿠키 빌드

        // 쿠키 제거하고 로그아웃 성공 메세지와 HTTP 200(성공) 응답 반환
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString()) // 응답 헤더에 쿠키 추가
                .body("Logged out successfully"); // 응답 바디에 로그아웃 성공 메세지 추가
    }

    /**
     * 아이디 중복 확인 요청을 처리하는 메서드
     * 클라이언트에서 아이디 중복 확인 요청이 들어왔을 때 해당 아이디가 이미 존재하는지 확인하고 결과 반환
     * @param userid 중복 확인을 요청한 사용자 아이디
     * @return 중복 여부에 맞는 메세지와 HTTP 응답코드
     */
    @GetMapping("/api/get/{userid}")
    public ResponseEntity<String> checkUseridDuplicate(@PathVariable("userid") String userid) {

        // 입력받은 아이디가 null이거나 빈값일경우
        if (!StringUtils.hasText(userid)) {
            log.info("AuthController.checkUseridDuplicate = userid bad request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("아이디를 입력해주세요.");
        }

        // 사용자 아아디 존재 여부 확인
        Boolean isUserExists = authService.checkUseridDuplicate(userid);

        // 아이디가 이미 존재하는 경우
        if (isUserExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 존재하는 아이디입니다.");
        }

        // 아이디가 사용 가능한 경우
        log.info("AuthController.checkUseridDuplicate - userid is available");
        return ResponseEntity.status(HttpStatus.OK)
                .body("사용 가능한 아이디 입니다.");
    }
}
