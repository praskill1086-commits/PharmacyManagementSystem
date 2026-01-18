package com.ey.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.ey.enums.AlertType;

public class CreateAlertRequest {

    @NotBlank
    private String message;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private AlertType type;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public AlertType getType() {
		return type;
	}

	public void setType(AlertType type) {
		this.type = type;
	}
    
    
}
