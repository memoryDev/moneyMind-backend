package dev.momory.moneymindbackend.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class ResponseDTO<T> {
    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private HttpStatus httpStatus;
    private String status;
    private T data;
    private String message;

    private ResponseDTO(HttpStatus httpStatus,String status, T data, String message) {
        this.httpStatus = httpStatus;
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ResponseDTO<T> successResponse(T data, String message) {
        return new ResponseDTO<>(HttpStatus.OK, SUCCESS_STATUS, data, message);
    }

    public static ResponseDTO<?> failMessageResponse(HttpStatus httpStatus, String message) {
        return new ResponseDTO<>(httpStatus, FAIL_STATUS, null, message);
    }

    public static ResponseDTO<?> failResponse(HttpStatus httpStatus, BindingResult bindingResult) {
        HashMap<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError)error).getField(), error.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }

        return new ResponseDTO<>(httpStatus, FAIL_STATUS, errors, null);
    }

    public static ResponseDTO<?> failResponse(HttpStatus httpStatus, String message) {
        log.warn("ResponseDTO.failResponse: {}", message);
        return new ResponseDTO<>(httpStatus, FAIL_STATUS, null, message);
    }

    public static ResponseDTO<?> errorResponse(HttpStatus httpStatus, String message) {
        return new ResponseDTO<>(httpStatus, ERROR_STATUS, null, message);
    }

}
