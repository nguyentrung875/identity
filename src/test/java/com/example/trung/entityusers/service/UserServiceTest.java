package com.example.trung.entityusers.service;

import com.example.trung.entityusers.dto.request.UserRequest;
import com.example.trung.entityusers.dto.response.UserResponse;
import com.example.trung.entityusers.entity.User;
import com.example.trung.entityusers.exception.CustomException;
import com.example.trung.entityusers.repository.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest //Spring test hỗ trợ giả dạng giống một api ở postman gọi request
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserSevice userSevice;

    @MockBean
    private UserRepo userRepo;


    private UserRequest request;
    private UserResponse userResponse;
    private User user;

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

        this.user = User.builder()
                .id("487a613b770f")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success(){
        //GIVEN

        Mockito.when(userRepo.existsByUsername(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepo.save(Mockito.any())).thenReturn(this.user);

        //WHEN
        var response =  userSevice.createUser(this.request);

        //THEN
        Assertions.assertThat(response.getId().equals("487a613b770f"));
        Assertions.assertThat(response.getUsername().equals("john"));
    }

    @Test
    void createUser_userExisted_fail(){
        //GIVEN

        Mockito.when(userRepo.existsByUsername(Mockito.anyString())).thenReturn(true);

        //WHEN
        //Trả về một biến chính là exception
        var exception = org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, ()-> userSevice.createUser(request));

        //THEN
        Assertions.assertThat(exception.getErrorStatus().getErrorCode()).isEqualTo(1001);
    }

    @Test //method này test cho trường hợp Happy case
    @WithMockUser(username = "admin")
    void getMyInfo_valid_success() throws Exception {
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        var response = userSevice.getMyInfo();

        Assertions.assertThat(response.getUsername().equals("john"));
        Assertions.assertThat(response.getId().equals("487a613b770f"));
    }


}
