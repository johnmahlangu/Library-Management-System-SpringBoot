package com.thokozanimahlangu.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Student entity.
 * Used for transporting student between the REST layer and the Service layer.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentDTO {

	private UUID id;
	
	@NotBlank(message = "Please enter the first name")
	private String firstName;
	
	@NotBlank(message = "Please enter the last name")
	private String lastName;
	
	@Email(message = "Please enter a valid email address")
	@NotBlank(message = "Please enter the email address")
	private String email;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime updateDate;
	
}
