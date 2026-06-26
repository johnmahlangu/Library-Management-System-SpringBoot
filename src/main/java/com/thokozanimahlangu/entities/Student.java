package com.thokozanimahlangu.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistence Entity representing a Student in the database.
 * Maps the business object to the 'student' table.
 * 
 * Stores information about:
 * - Student ID
 * - Student's first name
 * - Student's last name
 * - Student's email
 * - Created date
 * - Update date
 */

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Student")
public class Student {

	@Id
	@GeneratedValue(generator = "UUID")
	@UuidGenerator
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(name = "id", length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;
	
	@NotBlank
	@Size(max = 100)
	@Column(name = "first_name")
	private String firstName;
	
	@NotBlank
	@Size(max = 100)
	@Column(name = "last_name")
	private String lastName;
	
	@NotBlank
	@Size(max = 100)
	@Column(name = "email")
	private String email;
		
	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	@Column(name = "update_date")
	private LocalDateTime updateDate;

	@Builder.Default
	@OneToMany(mappedBy = "student", 
			   cascade = {CascadeType.MERGE, CascadeType.PERSIST},
			   orphanRemoval = true)
	private Set<IssueBook> issuedBooksToStudent = new HashSet<>();
	
	public void addStudentIssuedBook(IssueBook issuedBook) {		
		issuedBooksToStudent.add(issuedBook);
		issuedBook.setStudent(this);
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof Student)) return false;
	    Student student = (Student) o;
	    return id != null && id.equals(student.id);
	}

	@Override
	public int hashCode() {
	    return getClass().hashCode();
	}
}
