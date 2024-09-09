package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.dto.LogoutRequest;
import dev.momory.moneymindbackend.dto.TokenCategory;
import dev.momory.moneymindbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(
            @RequestBody LogoutRequest logoutRequest,
            RedirectAttributes redirectAttributes) {

        log.debug("AuthController.logout = {}", logoutRequest.getAccess());

        authService.logout(logoutRequest.getAccess(), redirectAttributes);

        // Refresh Token 쿠키에서 제거
        ResponseCookie cookie = ResponseCookie.from(TokenCategory.REFRESH.getValue(), "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body("Logged out successfully");


    }
}
