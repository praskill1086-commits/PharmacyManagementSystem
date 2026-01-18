package com.ey.mapper;

import com.ey.dto.request.CreateUserRequest;
import com.ey.dto.response.UserResponse;
import com.ey.entity.User;
import com.ey.enums.Role;

public class UserMapper {

    public static User toEntity(CreateUserRequest dto) {
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(dto.getPassword());
        u.setEmail(dto.getEmail());
        u.setPhone(dto.getPhone());
        u.setRole(Role.valueOf(dto.getRole()));
        u.setActive(true);
        return u;
    }

    public static UserResponse toResponse(User u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setUsername(u.getUsername());
        r.setEmail(u.getEmail());
        r.setPhone(u.getPhone());
        r.setRole(u.getRole().name());
        r.setActive(u.isActive());
        r.setCreatedAt(u.getCreatedAt());
        r.setUpdatedAt(u.getUpdatedAt());
        r.setUpdatedBy(u.getUpdatedBy() != null ? u.getUpdatedBy().getUsername() : null);
        return r;
    }
}
