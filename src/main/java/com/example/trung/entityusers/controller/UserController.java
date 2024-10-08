package com.example.trung.entityusers.controller;

import com.example.trung.entityusers.dto.APIResponse;
import com.example.trung.entityusers.dto.request.UserRequest;
import com.example.trung.entityusers.dto.request.UserUpdateRequest;
import com.example.trung.entityusers.dto.response.UserResponse;
import com.example.trung.entityusers.service.UserSevice;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/user")
public class UserController {
    UserSevice userSevice;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData(userSevice.createUser(userRequest));

        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequest request){
        APIResponse apiResponse = new APIResponse();


        apiResponse.setData(userSevice.updateUser(request));

        return new ResponseEntity<>(apiResponse,HttpStatus.ACCEPTED);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData(userSevice.getUserByUserName(username));

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<?> getMyInfo(){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData(userSevice.getMyInfo());

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
    @GetMapping("/getall")
    public ResponseEntity<?> getAllUser(){
        APIResponse apiResponse = new APIResponse();

        apiResponse.setData(userSevice.getAllUser());

        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}
