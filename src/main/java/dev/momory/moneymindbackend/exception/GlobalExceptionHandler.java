package dev.momory.moneymindbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * 유효성 검사 예외를 처리하는 메서드입니다.
     * @param ex 유효성 검사 실패시 발생합니다.
     * @return 유효성 검사 오류 메세지를 포함한 응답입니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        // 유효성 검사 오류를 저장합 맵을 생성
        Map<String, String> errors = new HashMap<>();

        // 모든 유효성 검사 오류를 반복하여 처리
        ex.getBindingResult().getAllErrors().forEach(e -> {
            // 필드 이름
            String fieldName = ((FieldError) e).getField();
            // 에러 메세지
            String errorMessage = e.getDefaultMessage();
            // 필드 이름과 에러 메세지 맵에 추가
            errors.put(fieldName, errorMessage);
        });

        // 오류가 발생한 맵과 HTTP 상태 코드 400 응답
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 오류가 발생했습니다. 다시 시도해 주세요.");
    }
}
