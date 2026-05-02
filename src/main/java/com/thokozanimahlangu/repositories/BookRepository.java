package com.thokozanimahlangu.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thokozanimahlangu.entities.Book;

/**
 * Repository interface for Book entity.
 * Provides abstraction for database operations using Spring Data JPA.
 */

public interface BookRepository extends JpaRepository<Book, UUID>{

	//Searches for books by title(case-insensitive) and returns matching books
	List<Book> findAllByTitleIsLikeIgnoreCase(String title);
	
	//Searches for books by author(case-insensitive) and returns matching books
	List<Book> findAllByAuthorIsLikeIgnoreCase(String author);
	
	//Searches for books by title and author (case-insensitive) and returns matching books
	List<Book> findAllByTitleIsLikeIgnoreCaseAndAuthorIsLikeIgnoreCase(String title, String author);
}
