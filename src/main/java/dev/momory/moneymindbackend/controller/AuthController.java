package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.dto.LogoutRequest;
import dev.momory.moneymindbackend.dto.SignUpRequest;
import dev.momory.moneymindbackend.dto.TokenCategory;
import dev.momory.moneymindbackend.entity.User;
import dev.momory.moneymindbackend.exception.CustomException;
import dev.momory.moneymindbackend.response.ResponseDTO;
import dev.momory.moneymindbackend.service.AuthService;
import dev.momory.moneymindbackend.util.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
//     * @param logoutRequest 로그아웃 요청 정보(Access Token 포함)
     * @param redirectAttributes 리다이렉트 시 사용할 속성
     * @return 로그아웃 성공 메세지를 포함한 HTTP 응답
     */
    @PostMapping("/api/logout")
    public ResponseDTO<?> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        String access = request.getHeader(TokenCategory.ACCESS.getValue());

        // access token이 없을경우
        if (StringUtils.isEmpty(access)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", "INVALID_TOKEN");
        }

        LogoutRequest logoutRequest = new LogoutRequest(access);

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
        response.setHeader("Set-Cookie", cookie.toString());
        return ResponseDTO.successResponse(null, "logout success");
    }

    /**
     * 아이디 중복 확인 요청을 처리하는 메서드
     * 클라이언트에서 아이디 중복 확인 요청이 들어왔을 때 해당 아이디가 이미 존재하는지 확인하고 결과 반환
     * @param userid 중복 확인을 요청한 사용자 아이디
     * @return 중복 여부에 맞는 메세지와 HTTP 응답코드
     */
    @GetMapping("/api/get/{userid}")
    public ResponseDTO<?> checkUseridDuplicate(@PathVariable("userid") String userid) {

        // 입력받은 아이디가 null이거나 빈값일경우
        if (!StringUtils.hasText(userid)) {
            log.warn("AuthController.checkUseridDuplicate = userid bad request");
            throw new CustomException(HttpStatus.BAD_REQUEST, "아이디를 입력해주세요", "USER_ID_REQUIRED");
        }

        // 사용자 아아디 존재 여부 확인
        authService.checkUseridDuplicate(userid);

        // 아이디가 사용 가능한 경우
        log.info("AuthController.checkUseridDuplicate - userid is available");
        return ResponseDTO.successResponse(null,"사용 가능한 아이디 입니다.");
    }

    /**
     * 회원가입 메서드
     * @param signUpRequest 회원가입할 사용자 정보 DTO
     * @return 응답
     */
    @PostMapping("/api/signup")
    public ResponseDTO<?> signupUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        // 회원가입 로직
        User savedEntity = authService.signupUser(signUpRequest);
        log.info("회원가입 성공 - 유저 아이디 = {}", savedEntity.getUserid());

        // 회원가입 성공 응답
        return ResponseDTO.successResponse(null, "회원가입 성공적으로 완료되었습니다.");
    }
}
