package com.thokozanimahlangu.entities;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

	@Id
	@GeneratedValue(generator = "UUID")
	@UuidGenerator
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;
	
	@NotBlank
	@Size(max = 30)
	private String author;
	
	@NotBlank
	@Size(max = 50)
	private String title;
	
	@NotNull
	private Year publicationYear;
	
	@NotBlank
	@Size(max = 13)
	private String ISBN;
	
	@CreationTimestamp
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	private LocalDateTime updatedDate;
	
	
	
	
}
