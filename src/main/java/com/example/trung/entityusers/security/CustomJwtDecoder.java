package com.example.trung.entityusers.security;

import java.text.ParseException;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import com.example.trung.entityusers.security.JwtUtilsHelper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.encodeKey}")
    private String encodedKey;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @SneakyThrows
    @Override
    public Jwt decode(String token) throws JwtException {

//        try {
////            var response = authenticationService.introspect(
////                    IntrospectRequest.builder().token(token).build());
////
//            boolean valid = jwtUtilsHelper.verifyToken(token);
//            if (!valid) throw new JwtException("Token invalid");
//
////            if (!response.isValid()) throw new JwtException("Token invalid");
//        } catch (JOSEException | ParseException e) {
//            throw new JwtException(e.getMessage());
//        }

        jwtUtilsHelper.verifyToken(token, false);

        if (Objects.isNull(nimbusJwtDecoder)) {
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey); //Giải mã key
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}