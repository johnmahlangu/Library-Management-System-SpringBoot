package com.thokozanimahlangu.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com.thokozanimahlangu.models.IssueStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistence Entity representing a IssueBook in the database.
 * Maps the business object to the 'IssueBook' table.
 */

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "IssueBook")
public class IssueBook {

	@Id
	@GeneratedValue(generator = "UUID")
	@UuidGenerator
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(name = "id", length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;
	
	@CreationTimestamp
	@Column(name = "issue_date", updatable = false)
	private LocalDateTime issueDate;
	
	@UpdateTimestamp
	@Column(name = "update_date")
	private LocalDateTime updateDate;
	
	@Column(name = "return_date")
	private LocalDate returnDate;
	
	@Column(name = "due_date")
	private LocalDate dueDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private IssueStatus status;
	
	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
}
