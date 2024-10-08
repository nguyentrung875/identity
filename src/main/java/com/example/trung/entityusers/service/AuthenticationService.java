package com.example.trung.entityusers.service;

import com.example.trung.entityusers.dto.request.RefeshTokenRequest;
import com.example.trung.entityusers.dto.request.SigninRequest;
import com.example.trung.entityusers.dto.request.SignoutRequest;
import com.example.trung.entityusers.dto.response.SigninResponse;
import com.example.trung.entityusers.entity.InvalidatedToken;
import com.example.trung.entityusers.entity.User;
import com.example.trung.entityusers.exception.CustomException;
import com.example.trung.entityusers.exception.ErrorStatus;
import com.example.trung.entityusers.repository.InvalidatedTokenRepo;
import com.example.trung.entityusers.repository.UserRepo;
import com.example.trung.entityusers.security.JwtUtilsHelper;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private UserRepo userRepo;
    private JwtUtilsHelper jwtUtilsHelper;
    private InvalidatedTokenRepo invalidatedTokenRepo;

    public SigninResponse signin(SigninRequest request) {
        SigninResponse signinResponse = new SigninResponse();
        boolean success = false;

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        success = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (success) {
            String token = jwtUtilsHelper.generateToken(user);
            signinResponse.setToken(token);
        } else {
            throw new CustomException(ErrorStatus.UNAUTHENTICATED);
        }

        return signinResponse;
    }

    public void signout(SignoutRequest request) throws ParseException, JOSEException {
        var signToken = jwtUtilsHelper.verifyToken(request.getToken(), false);

        String jti = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jti);
        invalidatedToken.setExpiryTime(expiryTime);

        invalidatedTokenRepo.save(invalidatedToken);
    }

    public SigninResponse refeshToken(RefeshTokenRequest request) throws ParseException, JOSEException {
        var signJWT = jwtUtilsHelper.verifyToken(request.getToken(), true);
        var jti = signJWT.getJWTClaimsSet().getJWTID();
        var expTime = signJWT.getJWTClaimsSet().getExpirationTime();

        //logout token cũ
        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jti);
        invalidatedToken.setExpiryTime(expTime);
        invalidatedTokenRepo.save(invalidatedToken);

        //issue token mới
        var username = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));
        var token = jwtUtilsHelper.generateToken(user);

        return new SigninResponse(token);
    }
}
