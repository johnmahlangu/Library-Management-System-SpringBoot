package com.thokozanimahlangu.models;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing the request to issue a book.
 * This object is used to capture incoming client input during the book checkout process.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IssueBookRequestDTO {
	
	// ID of the student borrowing the book
	private UUID studentId;
	
	// ID of the book being issued
	private UUID bookId;
	
	// Due date for returning the book
	@NotNull
	private LocalDate dueDate;
}
