package com.thokozanimahlangu.models;

import java.time.LocalDateTime;
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
	
	@NotBlank(message = "Please enter the book title")
	private String title;
	
	@NotBlank(message = "Please enter the author's name")
	private String author;
	
	@NotNull(message = "Please enter the publication Year")
	private Integer publicationYear;
	
	@NotBlank(message = "Please enter the ISBN")
	private String isbn;
	
	@Builder.Default
	private boolean available = true;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime updateDate;
}
