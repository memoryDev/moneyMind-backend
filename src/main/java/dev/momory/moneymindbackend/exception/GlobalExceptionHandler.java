package dev.momory.moneymindbackend.exception;

import dev.momory.moneymindbackend.response.ResponseDTO;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 유효성 검사 예외를 처리하는 메서드입니다.
     * @return 유효성 검사 오류 메세지를 포함한 응답입니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<?>> handleValidationExceptions(BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.failResponse(HttpStatus.BAD_REQUEST, bindingResult));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO<?>> handleCustomException(CustomException exception) {
        return ResponseEntity.status(exception.getStatus()).body(ResponseDTO.failResponse(exception.getStatus(), exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<?>> handleExceptions(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.errorResponse(HttpStatus.BAD_REQUEST,exception.getMessage()));
    }
}
