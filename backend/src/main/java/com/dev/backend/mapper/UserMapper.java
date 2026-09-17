package com.dev.backend.mapper;

import com.dev.backend.dto.response.UserResponse;
import com.dev.backend.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "name", target = "fullName")
    UserResponse userResponse(User user);


}
