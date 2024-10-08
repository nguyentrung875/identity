package com.example.trung.entityusers.mapper;

import com.example.trung.entityusers.dto.request.UserRequest;
import com.example.trung.entityusers.dto.request.UserUpdateRequest;
import com.example.trung.entityusers.dto.response.UserResponse;
import com.example.trung.entityusers.entity.Role;
import com.example.trung.entityusers.entity.User;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( user.getId() );
        userResponse.setUsername( user.getUsername() );
        userResponse.setFirstName( user.getFirstName() );
        userResponse.setLastName( user.getLastName() );
        userResponse.setDob( user.getDob() );
        Set<Role> set = user.getRoles();
        if ( set != null ) {
            userResponse.setRoles( new LinkedHashSet<Role>( set ) );
        }

        return userResponse;
    }

    @Override
    public User toUser(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( userRequest.getUsername() );
        user.password( userRequest.getPassword() );
        user.firstName( userRequest.getFirstName() );
        user.lastName( userRequest.getLastName() );
        user.dob( userRequest.getDob() );

        return user.build();
    }

    @Override
    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        user.setUsername( request.getUsername() );
        user.setPassword( request.getPassword() );
        user.setFirstName( request.getFirstName() );
        user.setLastName( request.getLastName() );
        user.setDob( request.getDob() );
    }
}
