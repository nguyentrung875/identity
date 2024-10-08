package com.example.trung.entityusers.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorStatus {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Error: ", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(8888, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001, "User already exits!", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found!", HttpStatus.NOT_FOUND),
    PASSWORD_INVALID(1003, "Password must be at least {min} charators", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1004, "You are not yet {min} years old", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Object not found!", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(2004, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2005, "You do not have permission", HttpStatus.FORBIDDEN),
    TOKEN_INVALID(2006, "Token invalid!", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRE(2007, "Token expire!", HttpStatus.UNAUTHORIZED);

    ;


    private int errorCode;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorStatus(){}
    ErrorStatus(int errorCode, String message, HttpStatusCode httpStatusCode) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}
