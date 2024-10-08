package com.example.trung.entityusers.security;

import com.example.trung.entityusers.entity.Permission;
import com.example.trung.entityusers.entity.Role;
import com.example.trung.entityusers.entity.User;
import com.example.trung.entityusers.enums.Role1;
import com.example.trung.entityusers.repository.PermissionRepo;
import com.example.trung.entityusers.repository.RoleRepo;
import com.example.trung.entityusers.repository.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    PermissionRepo permissionRepo;


    @Bean
    //Điều kiện cho Bean (Giải quyết vấn đề trong bài Isolation Unit test)
    //Bean này sẽ được khởi tạo khi datasource.driverClassName=com.mysql.cj.jdbc.Driver
    @ConditionalOnProperty(prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver"
    )
    ApplicationRunner applicationRunner(UserRepo userRepo, RoleRepo roleRepo){
        System.out.println("This it init Application......");
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {


                //Set role Admin với toàn bộ quyền
//                var permissions = permissionRepo.findAll();
                Role role = new Role();
                role.setName("ADMIN");
//                role.setPermissions(new HashSet<>(permissions));
                roleRepo.save(role);

                HashSet<Role> roles = new HashSet<>();
                roles.add(role);

                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));

                user.setRoles(roles);
                userRepo.save(user);
            };
        };
    }
}
