package com.thokozanimahlangu.repositories;


import java.time.Year;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.thokozanimahlangu.entities.Book;

/**
 * Utility class for building dynamic JPA queries for the Book entity.
 * These methods return a Specification, which Spring Data JPA converts into a SQL WHERE at runtime.
 */

public class BookSpecification {

	//Case-insensitive search for the book title.
	public static Specification<Book> hasTitle(String title) {
        return (root, query, cb) -> !StringUtils.hasText(title) ? null : 
               cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
	
	//Case-insensitive search for the book title.
	public static Specification<Book> hasAuthor(String author) {
        return (root, query, cb) -> !StringUtils.hasText(author) ? null : 
               cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }
	
	//Creates an exact match search for the ISBN.
	public static Specification<Book> hasISBN(String ISBN) {
        return (root, query, cb) -> !StringUtils.hasText(ISBN) ? null : 
               cb.equal(root.get("ISBN"), ISBN);
    }
	// Creates an exact match search for the publication year.
	public static Specification<Book> hasPublicationYear(Year publicationYear) {
        return (root, query, cb) -> publicationYear  == null ? null : 
               cb.equal(root.get("publicationYear"), publicationYear);
    }
}
