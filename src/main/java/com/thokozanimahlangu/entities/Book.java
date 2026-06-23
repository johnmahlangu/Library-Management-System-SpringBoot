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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistence Entity representing a Book in the database.
 * Maps the business object to the 'book' table.
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Book")
public class Book {

	@Id
	@GeneratedValue(generator = "UUID")
	@UuidGenerator
	@JdbcTypeCode(SqlTypes.CHAR)
	@Column(name = "id", length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
	private UUID id;
	
	@NotBlank
	@Size(max = 250)
	@Column(name = "author")
	private String author;
	
	@NotBlank
	@Size(max = 100)
	@Column(name = "title")
	private String title;
	
	@NotNull
	@Column(name = "publication_year")
	private Integer publicationYear;
	
	@NotBlank
	@Size(max = 17)
	@Column(name = "isbn")
	private String isbn;
	
	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;
	
	@UpdateTimestamp
	@Column(name = "update_date")
	private LocalDateTime updateDate;
	
	@Builder.Default
	@OneToMany(mappedBy = "book", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<IssueBook> issuedBooks = new HashSet<>();

	public void addIssuedBook(IssueBook issueBook) {		
		issuedBooks.add(issueBook);
		issueBook.setBook(this);
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof Book)) return false;
	    Book book = (Book) o;
	    return id != null && id.equals(book.id);
	}

	@Override
	public int hashCode() {
	    return getClass().hashCode();
	}
}
