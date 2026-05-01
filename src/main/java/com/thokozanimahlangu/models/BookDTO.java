package com.thokozanimahlangu.models;

import java.time.Year;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Book entity.
 * Used for transporting book between the REST layer and the Service layer.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookDTO {

	private UUID id;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String author;
	
	@NotNull
	private Year publicationYear;
	
	@NotBlank
	private String ISBN;
	
}
