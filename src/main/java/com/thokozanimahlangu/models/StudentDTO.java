package com.thokozanimahlangu.models;

import java.util.UUID;

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

	private UUID Id;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@NotBlank
	private String studentEmail;
}
