package com.thokozanimahlangu.web;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.thokozanimahlangu.exceptions.NotFoundException;
import com.thokozanimahlangu.models.BookDTO;
import com.thokozanimahlangu.services.BookService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookViewController {

	public static final String BOOKS_PATH = "/books";
	public static final String BOOK_PATH_ID = BOOKS_PATH + "/{bookId}";
	public static final String BOOKS_LIST_VIEW = "books/list"; 
	public static final String BOOK_DETAILS_VIEW = "books/details";
	
	private final BookService bookService;
	
	/**
	 * Handles GET requests to list books. Supports optional filtering by title, 
	 * author, publication year, and ISBN.
	 *
	 * @param title           optional filter for the book title
	 * @param author          optional filter for the book author
	 * @param publicationYear optional filter for the publication year
	 * @param isbn            optional filter for the book's ISBN
	 * @param model           the Spring UI Model to pass data to the view
	 * @return the path to the book list view template
	 */
	@GetMapping(BOOKS_PATH)
	public String listBooks(@RequestParam(required = false) String title,
							@RequestParam(required = false) String author,
							@RequestParam(required = false) Integer publicationYear,
							@RequestParam(required = false) String isbn,
							Model model) {
		
		// Fetch filtered books from the service layer and add them to the UI model
		model.addAttribute("books", bookService.listBooks(title, author, isbn, publicationYear));		
		// Render the book list HTML template
		return BOOKS_LIST_VIEW;
	}
	
	/**
	 * Handles GET requests to retrieve and display details for a specific book.
	 *
	 * @param bookId the unique identifier (UUID) of the book to retrieve
	 * @param model  the Spring UI Model to pass data to the view
	 * @return the path to the book details view template
	 */
	@GetMapping(BOOK_PATH_ID)
	public String getBook(@PathVariable UUID bookId, Model model) {
		
		// Fetch the book by ID; if it does not exist, throw a NotFoundException
		BookDTO book = bookService.getBookById(bookId)
				   .orElseThrow(NotFoundException::new);
		
		// Add existing book to the UI model
		model.addAttribute("book", book);
		// Render the book details HTML template
		return BOOK_DETAILS_VIEW;
	}
}
