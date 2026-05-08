package com.thokozanimahlangu.services;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.thokozanimahlangu.entities.Book;
import com.thokozanimahlangu.mappers.BookMapper;
import com.thokozanimahlangu.models.BookDTO;
import com.thokozanimahlangu.repositories.BookRepository;
import com.thokozanimahlangu.repositories.BookSpecification;

import lombok.RequiredArgsConstructor;

/**
 * Retrieves a list of books filtered by optional criteria.
 * Uses JPA Specifications for dynamic query building.
 */

@Service
@RequiredArgsConstructor
public class BookServiceJPA implements BookService{

	private final BookRepository bookRepository;
	private final BookMapper bookMapper;
	
	public List<BookDTO> listBooks(String title, String author, String ISBN, Year publicationYear) {
		
		// Build dynamic query criteria based on provided parameters
		Specification<Book> spec = Specification.where(BookSpecification.hasAuthor(author))
												.and(BookSpecification.hasTitle(title))
												.and(BookSpecification.hasPublicationYear(publicationYear))
												.and(BookSpecification.hasISBN(ISBN));
		
		// Fetch entities, map to DTOs, and return as a list
		return bookRepository.findAll(spec)
							.stream()
							.map(book -> bookMapper.bookToBookDTO(book))
							.collect(Collectors.toList());
					   
	}
	
	/**
     * Finds a single book by their unique UUID.
     * Returns an Optional to handle cases where the ID might not exist.
     */
	public Optional<BookDTO> getBookById(UUID id) {
		
		return bookRepository.findById(id)
							 .map(book -> bookMapper.bookToBookDTO(book));
	}
	
	/**
     * Persists a new book record.
     * Maps DTO to Entity for saving, then converts the result back to DTO.
     */
	public BookDTO saveNewBook(BookDTO newBook) {
		
		return bookMapper.bookToBookDTO(bookRepository.save(bookMapper.bookDTOtoBook(newBook)));
	}
	
	/**
     * Performs a full update of an existing book.
     * Overwrites the core fields regardless of whether they are null in the DTO.
     */
	public Optional<BookDTO> updateBookById(UUID id, BookDTO bookDto) {
		
		return bookRepository.findById(id)
				.map(foundBook -> {
					foundBook.setAuthor(bookDto.getAuthor());
					foundBook.setISBN(bookDto.getISBN());
					foundBook.setPublicationYear(bookDto.getPublicationYear());
					foundBook.setTitle(bookDto.getTitle());
					
					return bookMapper.bookToBookDTO(bookRepository.save(foundBook));
				});
	}
	
	/**
     * Deletes a book if they exist in the database.
     * @return true if deleted, false if the record was not found.
     */
	public Boolean deleteBookById(UUID id) {
		
		if (bookRepository.existsById(id)) {
			
			bookRepository.deleteById(id);
			
			return true;
		}		
		return false;
		}
	
	/**
     * Performs a partial update (Patch).
     * Only updates fields that are actually provided (not null/empty) in the DTO.
     */
	public Optional<BookDTO> patchBookById(UUID id, BookDTO bookDto) {
		
		return bookRepository.findById(id)
				.map(foundBook -> {
					if (StringUtils.hasText(bookDto.getAuthor())) {
						foundBook.setAuthor(bookDto.getAuthor());
					}
					if (StringUtils.hasText(bookDto.getISBN())) {
						foundBook.setISBN(bookDto.getISBN());
					}
					if (StringUtils.hasText(bookDto.getTitle())) {
						foundBook.setTitle(bookDto.getTitle());
					}
					if (bookDto.getPublicationYear() != null) {
						foundBook.setPublicationYear(bookDto.getPublicationYear());
					}
					
					return bookMapper.bookToBookDTO(bookRepository.save(foundBook));
				});
	}
}
