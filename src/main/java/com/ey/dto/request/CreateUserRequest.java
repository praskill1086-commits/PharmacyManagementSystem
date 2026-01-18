package com.ey.dto.request;

import jakarta.validation.constraints.*;

public class CreateUserRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;

    @NotNull
    private String role;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}   
    
    
}
