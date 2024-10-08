package com.example.trung.entityusers.security;

import com.example.trung.entityusers.dto.request.RefeshTokenRequest;
import com.example.trung.entityusers.dto.response.SigninResponse;
import com.example.trung.entityusers.entity.InvalidatedToken;
import com.example.trung.entityusers.entity.Role;
import com.example.trung.entityusers.entity.User;
import com.example.trung.entityusers.exception.CustomException;
import com.example.trung.entityusers.exception.ErrorStatus;
import com.example.trung.entityusers.repository.InvalidatedTokenRepo;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.temporal.TemporalUnit;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtUtilsHelper {
    @Value("${jwt.encodeKey}")
    private String encodedKey;

    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @Value("${jwt.refeshable-duration}")
    protected long REFESHABLE_DURATION;

    @Autowired
    private InvalidatedTokenRepo invalidatedTokenRepo;

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);


        Date curDate = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) //chuỗi cần tạo token
                .issuer("trungnguyen") //thường là domain
                .issueTime(curDate) //thời gian tạo token
                .jwtID(UUID.randomUUID().toString()) //Id của token
                .expirationTime(new Date(curDate.getTime() + VALID_DURATION * 1000)) //1000ms
                .claim("scope", this.buildScope(user)) //nội dung thêm vào VD: userID, tên user
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        try {
            signedJWT.sign(new MACSigner(decodedKey));
            return signedJWT.serialize();
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //Edit nội dung thêm vào claim
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }

    public SignedJWT verifyToken(String token, boolean isRefesh) throws ParseException, JOSEException {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

        SignedJWT signedJWT = SignedJWT.parse(token);

        JWSVerifier verifier = new MACVerifier(decodedKey);

        Date expTime = new Date();
        if (isRefesh){
            var resfesh = signedJWT.getJWTClaimsSet().getIssueTime()
                    .toInstant().plusSeconds(REFESHABLE_DURATION).toEpochMilli();
            expTime = new Date(resfesh);
        } else {
            expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        }

        boolean verified = signedJWT.verify(verifier) && expTime.after(new Date());

        //Kiểm tra token còn hiệu lực hay không
        if (!verified)
            throw new CustomException(ErrorStatus.UNAUTHENTICATED);

        //Kiểm tra token đó đã logout hay chưa (ktra trong table invalidated_token)
        if(invalidatedTokenRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
           throw new CustomException(ErrorStatus.TOKEN_INVALID);

        return signedJWT;
    }



//    public SignedJWT verifyAndGetSignedJWT(String token) throws ParseException, JOSEException {
//        if (this.verifyToken(token))
//            return SignedJWT.parse(token);
//        return null;
//    }
}
