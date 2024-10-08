package com.example.trung.entityusers.controller;

import com.example.trung.entityusers.dto.request.UserRequest;
import com.example.trung.entityusers.dto.response.UserResponse;
import com.example.trung.entityusers.repository.UserRepo;
import com.example.trung.entityusers.service.UserSevice;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@SpringBootTest //Spring test hỗ trợ giả dạng giống một api ở postman gọi request
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc //annotation dùng để tạo 1 mock request test
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserSevice userSevice;

    @MockBean
    private UserRepo userRepo;


    private UserRequest request;
    private UserResponse userResponse;

    @BeforeEach //method này chạy trước khi chạy test
    void initData(){ //Chuẩn bị dữ liệu đầu vào khi test
        var dob = LocalDate.of(1993,7,8);
        this.request = UserRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        this.userResponse = UserResponse.builder()
                .id("487a613b770f")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

    }

    @Test //method này test cho trường hợp Happy case
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUser_validRequest_success() throws Exception {
        log.info("Controller test: create User");
        //GIVEN: là những dữ liệu đầu vào mà ta dự đoán code đã xảy ra như vậy
        String content = objectMapper.writeValueAsString(this.request);

        //Chúng ta đang unit test controller nên  ko gọi trực tiếp đến method createUser từ service mà phải mock
        //Khi chạy unit test thì nó sẽ ko chạy vào method createUser ở UserService mà trả về thẳng userResponse
        Mockito.when(userSevice.createUser(ArgumentMatchers.any()))
                .thenReturn(this.userResponse);


        //WHEN: là khi chúng ta request api
        //THEN: khi when xảy ra thì ta mong đợi điều gì
        mockMvc.perform(MockMvcRequestBuilders
                .post("/user/create")
//                .header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ0cnVuZ25ndXllbiIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzI4MzYyNDk2LCJpYXQiOjE3MjgzNTg4OTYsImp0aSI6IjQ1N2JiMWUwLTkxZjAtNGY1NS1hMmE4LWRkNzkwYzE0NzgwNiIsInNjb3BlIjoiUk9MRV9BRE1JTiBDUkVBVEVfREFUQSBBUFBST1ZFX1BPU1QgVVBEQVRFX0RBVEEgUkVKRUNUX1BPU1QifQ.Fp3EtJYC20ci83aevkZeFFWn7axlbMNdwDG2e1eX9vQ8D7XwSTWFYiSEsF5w4NP7kNlKDtYwlqgqiMJ2qFPFyA")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
        ).andExpect(MockMvcResultMatchers.status().isOk()) //kết quả mong đợi là HttpStatus code là 200 //THEN
        .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
        .andExpect(MockMvcResultMatchers.jsonPath("data.id").value("487a613b770f")); //perform Tạo request
    }

    @Test //method này test cho trường hợp validation password
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUser_passwordInvalid_fail() throws Exception {
        request.setPassword("123456");
        //GIVEN: là những dữ liệu đầu vào mà ta dự đoán code đã xảy ra như vậy
        String content = objectMapper.writeValueAsString(this.request);


        //WHEN: là khi chúng ta request api
        //THEN: khi when xảy ra thì ta mong đợi điều gì
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/create")
//                .header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ0cnVuZ25ndXllbiIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzI4MzYyNDk2LCJpYXQiOjE3MjgzNTg4OTYsImp0aSI6IjQ1N2JiMWUwLTkxZjAtNGY1NS1hMmE4LWRkNzkwYzE0NzgwNiIsInNjb3BlIjoiUk9MRV9BRE1JTiBDUkVBVEVfREFUQSBBUFBST1ZFX1BPU1QgVVBEQVRFX0RBVEEgUkVKRUNUX1BPU1QifQ.Fp3EtJYC20ci83aevkZeFFWn7axlbMNdwDG2e1eX9vQ8D7XwSTWFYiSEsF5w4NP7kNlKDtYwlqgqiMJ2qFPFyA")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
        )
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest()) //kết quả mong đợi là HttpStatus code là 400 //THEN
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Password must be at least 8 charators")); //perform Tạo request
    }

    @Test
    @WithMockUser(username = "john")
    void getMyInfo_valid_success() throws Exception {
        Mockito.when(userSevice.getMyInfo())
                .thenReturn(this.userResponse);
        //WHEN: là khi chúng ta request api
        //THEN: khi when xảy ra thì ta mong đợi điều gì
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/myInfo")
//                .header("Authorization", "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ0cnVuZ25ndXllbiIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzI4MzYyNDk2LCJpYXQiOjE3MjgzNTg4OTYsImp0aSI6IjQ1N2JiMWUwLTkxZjAtNGY1NS1hMmE4LWRkNzkwYzE0NzgwNiIsInNjb3BlIjoiUk9MRV9BRE1JTiBDUkVBVEVfREFUQSBBUFBST1ZFX1BPU1QgVVBEQVRFX0RBVEEgUkVKRUNUX1BPU1QifQ.Fp3EtJYC20ci83aevkZeFFWn7axlbMNdwDG2e1eX9vQ8D7XwSTWFYiSEsF5w4NP7kNlKDtYwlqgqiMJ2qFPFyA")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
       )
                .andExpect(MockMvcResultMatchers.status()
                        .isOk()) //kết quả mong đợi là HttpStatus code là 400 //THEN
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data.username")
                        .value("john")); //perform Tạo request
    }


}
