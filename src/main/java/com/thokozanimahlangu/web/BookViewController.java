package com.thokozanimahlangu.web;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thokozanimahlangu.exceptions.NotFoundException;
import com.thokozanimahlangu.models.BookDTO;
import com.thokozanimahlangu.services.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookViewController {

	public static final String BOOKS_PATH = "/books";
	public static final String BOOKS_REDIRECT = "redirect:/books";
	public static final String BOOK_PATH_ID = BOOKS_PATH + "/{bookId}";
	public static final String BOOKS_LIST_VIEW = "books/list"; 
	public static final String BOOK_DETAILS_VIEW = "books/details";
	public static final String CREATE_BOOK_PATH = BOOKS_PATH + "/create";
	public static final String CREATE_BOOK_VIEW = "books/create"; 
	public static final String EDIT_BOOK_PATH = BOOKS_PATH + "/edit/{bookId}";
	public static final String EDIT_BOOK_VIEW = "books/edit";
	public static final String UPDATE_BOOK = BOOKS_PATH + "/update/{bookId}";
	public static final String DELETE_BOOK_PATH = BOOKS_PATH + "/delete/{bookId}";
	
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
	 * @throws NotFoundException if no book matches the provided UUID
	 * @return the path to the book details view template
	 */
	@GetMapping(BOOK_PATH_ID)
	public String getBook(@PathVariable UUID bookId, Model model) {
		
		// Fetch the book by ID; if it does not exist, throw a NotFoundException
		BookDTO book = bookService.getBookById(bookId)
				   .orElseThrow(NotFoundException::new);
		
		// Add existing book to the UI model;
		model.addAttribute("book", book);
		// Render the book details HTML template
		return BOOK_DETAILS_VIEW;
	}
	
	/**
	 * Handles GET requests to display the "Create New Book" form.
	 * Initializes an empty BookDTO to bind form fields.
	 *
	 * @param model the Spring UI Model to pass the empty DTO to the form view
	 * @return the path to the book creation form view template
	 */
	@GetMapping(CREATE_BOOK_PATH)
	public String createBookForm(Model model) {
		
		// Provide an empty DTO to the model
		model.addAttribute("book", new BookDTO());
		// Render the create book HTML template
		return CREATE_BOOK_VIEW;
	}
	
	/**
	 * Handles POST requests to process and save a new book submission.
	 * Validates the input data before persisting it via the service layer.
	 *
	 * @param bookDto the data transfer object containing the submitted form data
	 * @param result  captures any validation errors from the bound object
	 * @return a redirect path on success, or the form view path if validation fails
	 */
	@PostMapping(BOOKS_PATH)
	public String saveBook(@Valid @ModelAttribute("book") BookDTO bookDto, BindingResult result) {
		// If validation constraints fail, return to the create book form
		if(result.hasErrors()) {
			return CREATE_BOOK_VIEW;
		}
		
		bookService.saveNewBook(bookDto);
		// Redirect to prevent duplicate submissions on page refresh
		return BOOKS_REDIRECT;
	}
	
	/**
	 * Handles GET requests to display the "Edit Book" form populated with 
	 * the existing book's current details.
	 *
	 * @param bookId the unique identifier (UUID) of the book to edit
	 * @param model  the Spring UI Model to pass the book data to the form
	 * @return the path to the book editing form view template
	 * @throws NotFoundException if no book matches the provided UUID
	 */
	@GetMapping(EDIT_BOOK_PATH)
	public String editBookForm(@PathVariable UUID bookId, Model model) {
		
		BookDTO book = bookService.getBookById(bookId).orElseThrow(NotFoundException::new);
		
		model.addAttribute("book", book);
		// Render the edit book HTML template
		return EDIT_BOOK_VIEW;
	}
	
	/**
	 * Handles POST requests to process and save updates to an existing book.
	 * Validates the incoming form data before updating.
	 *
	 * @param bookId   the unique identifier (UUID) of the book being updated
	 * @param bookDto  the data transfer object containing the modified book data
	 * @param result   holds the results of the validation check
	 * @return the edit view if validation fails, or a redirect string if successful
	 */
	@PostMapping(UPDATE_BOOK)
	public String updateBook(@PathVariable UUID bookId, @Valid @ModelAttribute BookDTO bookDto, BindingResult result) {
		// If validation constraints fail, return to the edit form
		if(result.hasErrors()) {
			return EDIT_BOOK_VIEW;
		}
		
		bookService.updateBookById(bookId, bookDto);
		// Redirect to prevent duplicate updates on page refresh
		return BOOKS_REDIRECT;
	}
	
	/**
	 * Handles POST requests to delete a specific book by its ID.
	 *
	 * @param bookId the unique identifier (UUID) of the book to delete
	 * @return a redirect string to the books list page upon successful deletion
	 * @throws NotFoundException if the book to delete does not exist
	 */
	@PostMapping(DELETE_BOOK_PATH)
	public String deleteBook(@PathVariable UUID bookId) {
		// Attempt to delete the book; if the service returns false (not found), throw a 404
		if (!bookService.deleteBookById(bookId)) {
			throw new NotFoundException();
		}
		// Redirect to prevent duplicate deletions on page refresh
		return BOOKS_REDIRECT;
	}
}