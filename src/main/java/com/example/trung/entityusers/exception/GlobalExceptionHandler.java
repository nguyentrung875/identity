package com.example.trung.entityusers.exception;

import com.example.trung.entityusers.dto.APIResponse;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.lang.model.element.Element;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    //Bắt các lỗi không xác định
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<APIResponse> handlingRuntimException(Exception e) {
        APIResponse apiResponse = new APIResponse();

        apiResponse.setCode(500);
        apiResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Bắt các lỗi runtime đã custom
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<APIResponse> handlingCustomException(CustomException e) {
        ErrorStatus errorStatus = e.getErrorStatus();
        APIResponse apiResponse = new APIResponse();

        apiResponse.setCode(errorStatus.getErrorCode());
        apiResponse.setMessage(errorStatus.getMessage());

        return new ResponseEntity<APIResponse>(apiResponse, errorStatus.getHttpStatusCode());
    }

    //Bắt các lỗi về chứng thực
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<APIResponse> handlingAccessDeniedException(AccessDeniedException e) {
        ErrorStatus errorStatus = ErrorStatus.UNAUTHORIZED;

        APIResponse apiResponse = new APIResponse();
        apiResponse.setCode(errorStatus.getErrorCode());
        apiResponse.setMessage(errorStatus.getMessage());

        return new ResponseEntity<APIResponse>(apiResponse, errorStatus.getHttpStatusCode());
    }

    //Bắt các lỗi về Validation hoặc ko đúng field request
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handlingValidation(MethodArgumentNotValidException exception){
        //Lấy ra message mình đã set up bên Validation
        String enumKey = exception.getFieldError().getDefaultMessage();

        //Validation khi nhập sai Error VD: Key @Size(min = 8, message = "PASWORD_INVALID") thiểu chữ S
        ErrorStatus errorStatus = ErrorStatus.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            //Nếu đúng ErrorCode
            errorStatus = ErrorStatus.valueOf(enumKey);

            //Lấy attribute mà ta truyền vào Annotation
            var constraintViolation = exception.getBindingResult()
                    .getAllErrors().get(0).unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        } catch (IllegalArgumentException e) { //Lỗi Error Key không đúng

        }

        APIResponse apiResponse = new APIResponse();
        apiResponse.setCode(errorStatus.getErrorCode());
        //Gán attribute get được vào message
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                            this.mapAttribute(errorStatus.getMessage(), attributes)
                            : errorStatus.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes){
        String minValue = String.valueOf(attributes.get("min")) ;

        return message.replace("{" + "min" + "}", minValue);
    }


}


