package com.thokozanimahlangu.services;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.thokozanimahlangu.models.BookDTO;

/**
 * Service Interface for managing Book operations.
 * Defines the contract for business logic related to BookDTOs.
 */
public interface BookService {

	Optional<BookDTO> getBookById(UUID id);
	
	List<BookDTO> listBooks(String title, String author, String ISBN, Year publicationYear);
	
	BookDTO saveNewBook(BookDTO newBook);
	
	Optional<BookDTO> updateBookById(UUID id, BookDTO book);
	
	Boolean deleteBookById(UUID id);
	
	Optional<BookDTO> patchBookById(UUID id, BookDTO book);
}
