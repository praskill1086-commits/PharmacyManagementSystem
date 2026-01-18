package com.ey.service;

import java.util.List;

import com.ey.dto.request.CreateUserRequest;
import com.ey.dto.request.UpdateUserRequest;
import com.ey.dto.response.UserResponse;

public interface UserService {
	UserResponse create(CreateUserRequest request);
    List<UserResponse> getAll();
    UserResponse getById(Long id);
    UserResponse update(Long id, UpdateUserRequest request);
    void disable(Long id);
    void enable(Long id);

}
