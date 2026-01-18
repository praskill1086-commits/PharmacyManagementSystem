package com.ey.dto.request;

import jakarta.validation.constraints.*;

public class UpdateUserRequest {

    @NotBlank
    private String username;

    @Email
    private String email;

    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;

    @NotNull
    private String role;

    private boolean active;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

    
}
