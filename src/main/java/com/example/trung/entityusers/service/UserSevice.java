package com.example.trung.entityusers.service;

import com.example.trung.entityusers.dto.request.UserRequest;
import com.example.trung.entityusers.dto.request.UserUpdateRequest;
import com.example.trung.entityusers.dto.response.UserResponse;
import com.example.trung.entityusers.entity.User;
import com.example.trung.entityusers.exception.CustomException;
import com.example.trung.entityusers.exception.ErrorStatus;
import com.example.trung.entityusers.mapper.UserMapper;
import com.example.trung.entityusers.repository.RoleRepo;
import com.example.trung.entityusers.repository.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserSevice {
    UserRepo userRepo;
    UserMapper userMapper;
    RoleRepo roleRepo;

    public UserResponse createUser(UserRequest userRequest){
        UserResponse userResponse = new UserResponse();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (userRepo.existsByUsername(userRequest.getUsername())){
            throw new CustomException(ErrorStatus.USER_EXISTED);
        }

        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

//        HashSet<String> roles = new HashSet<>();
//        roles.add(Role.USER.name());

//        user.setRoles(roles);

        return userMapper.toUserResponse(userRepo.save(user));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')") //chặn trước khi vào method
    @PostAuthorize("returnObject.username == authentication.name") //chặn sau khi method thực hiện xong (ít sử dụng)
    public  UserResponse updateUser(UserUpdateRequest request){
        UserResponse userResponse = new UserResponse();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //Cập nhật lại role
        var roles = roleRepo.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepo.save(user));
    }

    //User chỉ lấy được thông tin của chính mình, ko lấy đc của ng khác
    public UserResponse getMyInfo(){
        //Khi user đăng nhập thành công thì thông tin của user được lưu trữ trong SecurityContextHolder
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        System.out.println("getAuthorities: " + context.getAuthentication().getAuthorities());

        User user = userRepo.findByUsername(name).orElseThrow(
                () -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    //Sau khi nhận đc UserResponse thì kiểm tra xem user trả về có đúng với user đang đăng nhập hay ko
    //Nếu đúng thì mới trả về kết quả
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserByUserName(String usernameRequest) {
        UserResponse userResponse = new UserResponse();
        User user = userRepo.findByUsername(usernameRequest)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponse> getAllUser(){
        List<UserResponse> userResponseList = new ArrayList<>();
        List<User> users = userRepo.findAll();

        for (User user : users){
            userResponseList.add( userMapper.toUserResponse(user));
        }

        return userResponseList;
    }
}
