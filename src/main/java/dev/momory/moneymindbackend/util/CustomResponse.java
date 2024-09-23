package dev.momory.moneymindbackend.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomResponse<T> {

    private CustomHeader header;
    private T data;
    private String msg;

    private CustomResponse(CustomHeader header, T data, String msg) {
        this.header = header;
        this.data = data;
        this.msg = msg;
    }

    public static <T> CustomResponse<T> success(T data, String msg) {
        return new CustomResponse<T>(
                new CustomHeader(HttpStatus.OK.value(),
                                 HttpStatus.OK.getReasonPhrase()
                ),
                data,
                msg
        );
    }

    public static <T> CustomResponse<T> fail( HttpStatus httpStatus, T data) {
        return null;
//        return new CustomResponse<T>(
//                new CustomHeader(.getHttpStatus(), responseCode.getMessage()),
//                data,
//                responseCode.getMessage()
//        );
    }
}
