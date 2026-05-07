package com.thokozanimahlangu.controllers;

import java.time.Year;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thokozanimahlangu.models.BookDTO;
import com.thokozanimahlangu.services.BookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookController {

	public static final String BOOK_PATH = "/api/book";
	public static final String BOOK_PATH_ID = BOOK_PATH + "/{bookId}";
	
	private final BookService bookService;
	
	/**
     * Partially update an existing book
     * Validates input and returns 204 No Content upon success
     */
	@PatchMapping(BOOK_PATH_ID)
	public ResponseEntity<?> patchBook(@PathVariable("bookId") UUID id, @Validated @RequestBody BookDTO book) {
		
		bookService.patchBookById(id, book);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
     * Delete a book by ID
     * Throws NotFoundException (404) if the book does not exist
     */
	@DeleteMapping(BOOK_PATH_ID)
	public ResponseEntity<?> deleteBook(@PathVariable("bookId") UUID id) {
		
		if (!bookService.deleteBookById(id)) {
			
			throw new NotFoundException();
		}		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
     * Create a new book record
     * Returns 201 Created and includes the 'Location' header pointing to the new book
     */
	@PostMapping(BOOK_PATH)
	public ResponseEntity<?> handlePost(@Validated @RequestBody BookDTO bookDto) {
		
		BookDTO newBook = bookService.saveNewBook(bookDto);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", BOOK_PATH + "/" + newBook.getId());
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
     * Replace an existing book with new data
     * Uses Optional.isEmpty() to verify existence; throws 404 if missing
     */
	@PutMapping(BOOK_PATH_ID)
	public ResponseEntity<?> updateBook(@PathVariable("bookId") UUID id, @Validated @RequestBody BookDTO bookDto) {
		
		if (bookService.updateBookById(id, bookDto).isEmpty()) {
			
			throw new NotFoundException();
		}		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
     * Retrieve a single book by its UUID
     * Directly returns the DTO, mapping the empty Optional to a 404 exception
     */
	@GetMapping(BOOK_PATH_ID)
	public BookDTO getBook(@PathVariable("bookId") UUID id) {
		
		return bookService.getBookById(id).orElseThrow(NotFoundException::new);
	}
	
	/**
     * List all books with optional filtering
     * Filters are passed as query parameters
     */
	@GetMapping(BOOK_PATH)
	public List<BookDTO> listBooks(@RequestParam(required = false) String firstName,
								   @RequestParam(required = false) String lastName,
							       @RequestParam(required = false) String email,
								   @RequestParam(required = false) Year publicationYear) {
		
		return bookService.listBooks(firstName, lastName, email, publicationYear);
	}		
}
