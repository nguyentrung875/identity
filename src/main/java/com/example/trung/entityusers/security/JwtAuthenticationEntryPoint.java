package com.example.trung.entityusers.security;

import com.example.trung.entityusers.dto.APIResponse;
import com.example.trung.entityusers.exception.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.awt.*;
import java.io.IOException;

//Xử lý ngoại lệ 401 do Filter của Spring Security chặn
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorStatus errorStatus = ErrorStatus.UNAUTHENTICATED;

        response.setStatus(errorStatus.getHttpStatusCode().value());
        //Trả về 1 body với ContentType là Json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        APIResponse apiResponse = new APIResponse();
        apiResponse.setMessage(errorStatus.getMessage());
        apiResponse.setCode(errorStatus.getErrorCode());

        ObjectMapper objectMapper = new ObjectMapper();

        //.getWrite viết nội dung cần trả về
        //.write(args) có args là String nên ta dùng ObjectMapper để map APIResponse vể Json
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer(); //gửi request về cho client
    }
}
