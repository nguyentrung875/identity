package com.example.trung.entityusers.controller;

import com.example.trung.entityusers.dto.request.UserCreationRequest;
import com.example.trung.entityusers.dto.request.UserRequest;
import com.example.trung.entityusers.dto.response.UserResponse;
import com.example.trung.entityusers.service.UserSevice;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc //need this in Spring Boot test
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSevice userSevice;

    private UserRequest request;
    private UserResponse response;

    @BeforeEach //method này chạy trước khi chạy test
    void initData(){
        var dob = LocalDate.of(1993,7,8);
        request = UserRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

    }

    @Test
    void createUser(){

        //GIVEN: là những dữ liệu đầu vào mà ta dự đoán code đã xảy ra như vậy

        //WHEN: là khi chúng ta request api
        mockMvc.perform(MockMvcRequestBuilders.post())
        //THEN: khi when xảy ra thì ta mong đợi điều gì

    }
}
