package com.example.trung.entityusers.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //bật để cài đặt phân quyền trên method
public class CustomSecurityConfiguration {
//    private final String[] PUBLIC_ENDPOINTS = {"/auth/**"};
    private final String[] PUBLIC_ENDPOINTS = {"/auth/**"};


    @Value("${jwt.encodeKey}")
    private String encodedKey;

    @Autowired
    CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("Hello filterchain!");
        http.cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
//                        .requestMatchers("/user/getall").hasAuthority("ROLE_ADMIN") //Chỉ cho phép truy cập với role Admin
//                        .requestMatchers("/user/getall").hasRole("ADMIN") //hasRole thì phải có prefix SCOPE_
//                        .requestMatchers("/user/update").hasRole(Role.ADMIN.name()) //Cách viết khác
                        .anyRequest().authenticated()
                );

        //Cấu hình jwt bằng Oauth2 thì dùng prefix SCOPE_ còn cấu hình theo provider thì dùng prefix ROLE_
        http.oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // Xử lý ngoại lệ 401
                        .jwt(jwt -> jwt //Setup Authentication với JWT
                            .decoder(customJwtDecoder)
                            .jwtAuthenticationConverter(jwtAuthenticationConverter()))); //Convert SCOPE_ -> ROLE_
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        //Tùy chỉnh lại phân quyền, thay vì dùng "SCOPE_" theo mặc định trong claims thì dùng "ROLE_"
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");//Vì ta đã chủ động gán ROLE_ ở buildScope() nên ta bỏ Prefix ở đây

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    //Khóa học Java spring boot 3: #24 cấu hình CORS và integrate với Front End bằng React JS
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("http://localhost:3000"); //Cho phé frontend truy cập api
        corsConfiguration.addAllowedMethod("*"); //cho phép tất cả method
        corsConfiguration.addAllowedHeader("*"); //cho phép tất cả header
        //Khai báo các tầng endpoint
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        //apply cho toàn bộ endpoint
        urlBasedCorsConfigurationSource.registerCorsConfiguration("*", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

//    @Bean
//    public JwtDecoder jwtDecoder(){ //chịu trách nhiệm verify token
//        byte[] decodedKey = Base64.getDecoder().decode(encodedKey); //Giải mã key
//        //Tạo secretKey từ secretString
//        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HS512");
//        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder
//                .withSecretKey(secretKey)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//        return nimbusJwtDecoder;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
