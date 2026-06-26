package com.thokozanimahlangu.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used to return the details of an issued book record.
 * This class flattens data from the IssueBook entity and its related Student and Book entities into a single payload for the client/frontend.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IssueBookResponseDTO {

	// Unique issue transaction ID
	private UUID id;
	
	// Student information
	private UUID studentId;
	private String firstName;
	private String lastName;	
	private String email;
	
	// Book information
	private UUID bookId;
	private String bookTitle;
	
	// Issue transaction details
	private LocalDate issueDate;
	private IssueStatus status;	
	private LocalDate dueDate;	
	private LocalDate returnDate;	
	private LocalDateTime updateDate;	
}
