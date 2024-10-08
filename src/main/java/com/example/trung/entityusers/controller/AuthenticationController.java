package com.example.trung.entityusers.controller;

import com.example.trung.entityusers.dto.APIResponse;
import com.example.trung.entityusers.dto.request.RefeshTokenRequest;
import com.example.trung.entityusers.dto.request.SigninRequest;
import com.example.trung.entityusers.dto.request.SignoutRequest;
import com.example.trung.entityusers.dto.response.SigninResponse;
import com.example.trung.entityusers.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @PostMapping ("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest request){
        APIResponse apiResponse = new APIResponse();

        SigninResponse signinResponse = authenticationService.signin(request);
        apiResponse.setData(signinResponse);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping ("/signout")
    public ResponseEntity<?> signout(@RequestBody SignoutRequest request) throws ParseException, JOSEException {
        APIResponse apiResponse = new APIResponse();

        authenticationService.signout(request);
        apiResponse.setMessage("Signed out!");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping ("/refesh")
    public ResponseEntity<?> signin(@RequestBody RefeshTokenRequest request) throws ParseException, JOSEException {
        APIResponse apiResponse = new APIResponse();

        SigninResponse signinResponse = authenticationService.refeshToken(request);
        apiResponse.setData(signinResponse);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }



}
