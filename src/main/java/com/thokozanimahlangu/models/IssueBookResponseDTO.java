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
 * This class flattens data from the IssueBook entity and its related Student and Book entities into a single, clean payload for the client/frontend.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IssueBookResponseDTO {

	private UUID id;
	
	private UUID studentId;
	
	private UUID bookId;
	
	private String bookTitle;
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate issueDate;
	
	private LocalDate dueDate;
	
	private LocalDateTime updateDate;
}
