package com.ey.service;

public interface AuthService {
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
