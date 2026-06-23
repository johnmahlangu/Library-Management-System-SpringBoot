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
	
	private UUID studentId;
	
	private UUID bookId;
	
	@NotNull
	private LocalDate dueDate;
}
