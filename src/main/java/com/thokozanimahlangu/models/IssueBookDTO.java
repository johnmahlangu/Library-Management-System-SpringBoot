package com.thokozanimahlangu.models;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for IssueBook entity.
 * Used for transporting IssueBook between the REST layer and the Service layer.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IssueBookDTO {

	private UUID bookId;
	private UUID studentId;
	private LocalDateTime issueDate;
	private LocalDateTime dueDate;
}
