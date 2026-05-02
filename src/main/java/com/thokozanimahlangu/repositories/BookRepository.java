package com.thokozanimahlangu.repositories;

import java.time.Year;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thokozanimahlangu.entities.Book;

/**
 * Repository interface for Book entity.
 * Provides automated query execution for searching books based on various attributes.
 * Provides abstraction for database operations using Spring Data JPA.
 */

public interface BookRepository extends JpaRepository<Book, UUID>{

	//Searches for books by title and returns matching books
	List<Book> findAllByTitleIsLikeIgnoreCase(String title);
	
	//Searches for books by year published and returns matching books
	List<Book> findAllByPublicationYear(Year publicationYear);
	
	//Searches for books by ISBN and returns matching books
	List<Book> findAllByISBN(String ISBN);
	
	//Searches for books by author and returns matching books
	List<Book> findAllByAuthorIsLikeIgnoreCase(String author);
	
	//Searches for books by title and publication year and returns matching books
	List<Book> findAllByTitleIsLikeIgnoreCaseAndPublicationYear(String title, Year publicationYear);
	
	//Searches for books by title and ISBN and returns matching books
	List<Book> findAllByTitleIsLikeIgnoreCaseAndISBN(String title, String ISBN);
	
	//Searches for books by year and ISBN and returns matching books
	List<Book> findAllByYearAndISBN(Year publicationYear, String ISBN);
	
	//Searches for books by year and author and returns matching books
	List<Book> findAllByPublicationYearAndAuthorIsLikeIgnoreCase(Year publicationYear, String author);
	
	//Searches for books by ISBN and and author and returns matching books
	List<Book> findAllByISBNAndAuthorIsLikeIgnoreCase(String ISBN, String author);
	
	//Searches for books by title and author and returns matching books
	List<Book> findAllByTitleIsLikeIgnoreCaseAndAuthorIsLikeIgnoreCase(String title, String author);
	
	//Searches for books by title, publication year, ISBN and returns matching books
	List<Book> findAllByTitleIsLikeIgnoreCaseAndPublicationYearAndISBN(String title, Year publicationYear, String ISBN);
	
	//Searches for books by title, publication year, author and returns matching books
	List<Book> findAllByTitleIsLikeIgnoreCaseAndPublicationYearAndAuthorIsLikeIgnoreCase(String title, Year publicationYear, String author);
	
	//Searches for books by year, ISBN, author and returns matching books
	List<Book> findAllByPublicationYearAndISBNAndAuthorIsLikeIgnoreCase(Year publicationYear, String ISBN, String author);
	
}
