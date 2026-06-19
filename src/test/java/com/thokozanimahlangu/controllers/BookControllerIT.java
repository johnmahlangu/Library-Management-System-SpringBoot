package com.thokozanimahlangu.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.thokozanimahlangu.entities.Book;
import com.thokozanimahlangu.mappers.BookMapper;
import com.thokozanimahlangu.models.BookDTO;
import com.thokozanimahlangu.repositories.BookRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration Tests for the  BookController.
 * This class boots up the complete Spring application context (`@SpringBootTest`)  and configures `MockMvc` to test the HTTP end-points without
 * spinning up a real server.
 * It interacts directly with the database to seed data, perform actions via HTTP, and assert state changes.
 */
@SpringBootTest
class BookControllerIT {

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	BookController bookController;
	
	@Autowired
	BookMapper bookMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	WebApplicationContext webAppContext;
	
	MockMvc mockMvc;
	
	@BeforeEach
	void setUp() {
		// Initialize MockMvc using the full application web context
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	}
	
	/**
	 * Tests fetching all books when data exists.
	 * Verifies the response size matches the exact count of records in the database.
	 */
	@Test
	void listBooks() throws Exception{
		
		long databaseBookCount = bookRepository.count();
		
		mockMvc.perform(get(BookController.BOOK_PATH)
				   .accept(MediaType.APPLICATION_JSON))
				   .andExpect(status().isOk())
				   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				   .andExpect(jsonPath("$.length()", is((int)databaseBookCount)));
	}
	
	/**
	 * Tests fetching books when the database record is completely empty.
	 */
	@Rollback
	@Transactional
	@Test
	void emptyList() throws Exception{			

		bookRepository.deleteAll();
		
		mockMvc.perform(get(BookController.BOOK_PATH)
				   .accept(MediaType.APPLICATION_JSON))
				   .andExpect(status().isOk())
				   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				   .andExpect(jsonPath("$.length()", is(0)));		
	}
	
	/**
	 * Tests retrieving a single book using a valid, existing ID.
	 * Asserts that all returned JSON fields match the database record exactly.
	 */
	@Test
	void getBookById() throws Exception{

		Book book = bookRepository.findAll().get(0);
		UUID bookId = book.getId();
		
		mockMvc.perform(get(BookController.BOOK_PATH_ID, bookId)
				   .accept(MediaType.APPLICATION_JSON))
				   .andExpect(status().isOk())
				   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				   .andExpect(jsonPath("$.id", is(bookId.toString())))
				   .andExpect(jsonPath("$.author", is(book.getAuthor())))
				   .andExpect(jsonPath("$.title", is(book.getTitle())))
				   .andExpect(jsonPath("$.isbn", is(book.getIsbn())))
				   .andExpect(jsonPath("$.publicationYear", is(book.getPublicationYear())));		
	}
	
	/**
	 * Tests retrieving a book with a non-existent random UUID.
	 * Expects a 404 Not Found response.
	 */
	@Test
	void bookIdNotFound() throws Exception{		
		
		mockMvc.perform(get(BookController.BOOK_PATH_ID, UUID.randomUUID()))				   
				   .andExpect(status().isNotFound());
	}
	
	/**
	 * Tests the creation of a new book.
	 * Extracts the new resource path from the 'Location' header to verify database persistence.
	 */
	@Rollback
	@Transactional
	@Test
	void saveNewBook() throws Exception{		
	
		BookDTO bookDto = createValidBook();
				
		MvcResult result = mockMvc.perform(post(BookController.BOOK_PATH)
				   .accept(MediaType.APPLICATION_JSON)
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(bookDto)))
				   .andExpect(status().isCreated())
				   .andExpect(header().exists("Location"))
				   .andReturn();
		
		// Extract the newly generated UUID from the 'Location' header path
		String headerLocation = result.getResponse().getHeader("Location");
		String[] UUIDlocation = headerLocation.split("/");
		UUID savedUUID = UUID.fromString(UUIDlocation[4]);
		
		// Fetch from DB using the extracted UUID and verify fields matched the DTO
		Book book = bookRepository.findById(savedUUID).orElse(null);
		assertThat(book).isNotNull();
		assertThat(book.getAuthor()).isEqualTo(bookDto.getAuthor());
		assertThat(book.getTitle()).isEqualTo(bookDto.getTitle());
		assertThat(book.getIsbn()).isEqualTo(bookDto.getIsbn());
		assertThat(book.getPublicationYear()).isEqualTo(bookDto.getPublicationYear());
	}
	
	/**
	 * Tests a complete update (PUT) of an existing book.
	 * Modifies a field, issues PUT, and verifies the update was saved to the database.
	 */
	@Rollback
	@Transactional
	@Test
	void updateBook() throws Exception {

		Book book = bookRepository.findAll().get(0);
		BookDTO bookDto = bookMapper.bookToBookDTO(book);	
		
		// Update a specific field in the payload
		bookDto.setAuthor("Paul Skhosana");
		
		mockMvc.perform(put(BookController.BOOK_PATH_ID, book.getId())
				   .accept(MediaType.APPLICATION_JSON)
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(bookDto)))
				   .andExpect(status().isNoContent());
		
		// Retrieve the entity directly from DB to verify changes stuck
		Book updatedBook = bookRepository.findById(book.getId()).get();
		assertThat(updatedBook.getAuthor()).isEqualTo("Paul Skhosana");
	}
	
	/**
	 * Tests updating a book resource that does not exist.
	 * Expects a 404 Not Found response.
	 */
	@Test
	void updateBookNotFoundId() throws Exception{	

		BookDTO bookDto = createValidBook();
		
		mockMvc.perform(put(BookController.BOOK_PATH_ID, UUID.randomUUID())
				   .accept(MediaType.APPLICATION_JSON)
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(bookDto)))		   
				   .andExpect(status().isNotFound());
	}
	
	/**
	 * Tests deleting a book.
	 * Verifies that the resource returns 204 NoContent, and is gone from the database.
	 */
	@Transactional
	@Rollback
	@Test
	void deleteBookById() throws Exception {
		
		Book book = bookRepository.findAll().get(0);
		
		mockMvc.perform(delete(BookController.BOOK_PATH_ID, book.getId())
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isNoContent());
		
		boolean exists = bookRepository.existsById(book.getId());
		assertFalse(exists);
	}
	
	/**
	 * Tests deleting a book that does not exist.
	 * Expects a 404 Not Found response.
	 */
	@Test
	void deleteByIdNotFound() throws Exception {
		
		mockMvc.perform(delete(BookController.BOOK_PATH_ID, UUID.randomUUID())
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isNotFound());
	}
	
	/**
	 * Tests partial modification (PATCH) of a book.
	 * Sends a partial map containing only the title, and verifies the update persists.
	 */
	@Transactional
	@Rollback
	@Test
	void patchBook() throws Exception {
		
		Book book = bookRepository.findAll().get(0);		
		Map<String, String> patchBook = Map.of("title", "NewTitle");
		
		mockMvc.perform(patch(BookController.BOOK_PATH_ID, book.getId())
			   .accept(MediaType.APPLICATION_JSON)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(patchBook)))
			   .andExpect(status().isNoContent());
		
		Book patchedBook = bookRepository.findAll().get(0);
		assertThat(patchBook.get("title")).isEqualTo(patchedBook.getTitle());
	}
	
	/**
	 * Tests patching a book that does not exist.
	 * Expects a 404 Not Found response.
	 */
	@Test
	void patchBookNotFoundId() throws Exception {
		
		Map<String, String> patchBook = Map.of("title", "NewTitle");
		
		mockMvc.perform(patch(BookController.BOOK_PATH_ID, UUID.randomUUID())
				   .accept(MediaType.APPLICATION_JSON)
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(patchBook)))
				   .andExpect(status().isNotFound());
	}
	
	/**
	 * Helper method to construct a valid BookDTO for creation payloads.
	 * return a pre-populated BookDTO instance.
	 */
	private BookDTO createValidBook() {
		return BookDTO.builder()
				      .author("john")
				      .title("programming")
				      .isbn("987-3647582698")
				      .publicationYear(2017)
				      .build();
	}
}