package com.thokozanimahlangu.controllers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.thokozanimahlangu.models.BookDTO;
import com.thokozanimahlangu.services.BookService;

import tools.jackson.databind.ObjectMapper;

/**
 * Slice test for Book Controller.
 * 
 * This class tests the web layer in isolation by verifying HTTP requests, status codes, payload validation, and JSON serialization.
 */
@WebMvcTest(BookController.class)
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

	@Autowired
	MockMvc mockMvc; // Simulates HTTP requests without starting a real server
	
	@Autowired
	ObjectMapper objectMapper; // Handles Java-to-JSON serialization for request bodies
	
	@MockitoBean
	BookService bookService; // Mocked service layer
	
	@Captor
	ArgumentCaptor<BookDTO> bookCaptor; // Captures payloads passed to service methods
	
	
	/**
	 * Verifies that a valid UUID returns the matching book details with a 200 OK status.
	 */
	@Test
	void getBookById() throws Exception {
								  	
		BookDTO bookDto = createValidBook();
		UUID bookId = bookDto.getId();
		
		given(bookService.getBookById(any(UUID.class))).willReturn(Optional.of(bookDto));
		
		mockMvc.perform(get(BookController.BOOK_PATH_ID, bookId)
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.id", is(bookId.toString())))
			   .andExpect(jsonPath("$.author", is(bookDto.getAuthor())))
			   .andExpect(jsonPath("$.title", is(bookDto.getTitle())))
			   .andExpect(jsonPath("$.isbn", is(bookDto.getIsbn())))
			   .andExpect(jsonPath("$.publicationYear", is(bookDto.getPublicationYear())));
	}
	
	/**
	 * Verifies that searching for a non-existent book ID correctly yields a 404 Not Found.
	 */
	@Test
	void getBookByIdNotFound() throws Exception {
		
		given(bookService.getBookById(any(UUID.class))).willReturn(Optional.empty());
		
		mockMvc.perform(get(BookController.BOOK_PATH_ID, UUID.randomUUID()))
			   .andExpect(status().isNotFound());
	}
	
	/**
	 * Verifies that pulling a collection of books returns the expected list and size with a 200 Ok status.
	 */
	@Test
	void listBooks() throws Exception {
		
		given(bookService.listBooks(any(), any(), any(), any())).willReturn(List.of(createValidBook(), createValidBook()));
		
		mockMvc.perform(get(BookController.BOOK_PATH)
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.length()", is(2)));
	}
	
	/**
	 * Verifies that a valid book creation request returns a 201 Created status,
	 * includes a Location header, and correctly passes data down to the service layer.
	 */
	@Test
	void saveBook() throws Exception {
		
		BookDTO bookDto = createValidBook();
		
		given(bookService.saveNewBook(any(BookDTO.class))).willReturn(bookDto);
		
		mockMvc.perform(post(BookController.BOOK_PATH)
			   .accept(MediaType.APPLICATION_JSON)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(bookDto)))
			   .andExpect(status().isCreated())
			   .andExpect(header().exists("Location"));
		
		// Verify the exact state of the book sent to the service layer
		verify(bookService).saveNewBook(bookCaptor.capture());	
		assertThat(bookCaptor.getValue())
							 .usingRecursiveComparison()
							 .ignoringFields("createdDate", "updateDate")
							 .isEqualTo(bookDto);
	}
	
	/**
	 * Verifies that a POST request with missing required fields triggers MethodArgumentNotValidException and returns a 400 Bad Request.
	 */
	@Test
	void saveBookWithMissingRequiredFields() throws Exception {
		
		BookDTO bookDto = BookDTO.builder().build();
		
			mockMvc.perform(post(BookController.BOOK_PATH)
				   .accept(MediaType.APPLICATION_JSON)
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(bookDto)))
				   .andExpect(status().isBadRequest())
				   .andDo(print());	
	}
	
	/**
	 * Verifies that updating an existing book returns a 204 No Content status
	 */
	@Test
	void updateBook() throws Exception {
		
		BookDTO bookDto = createValidBook();
		
		given(bookService.updateBookById(any(UUID.class), any(BookDTO.class))).willReturn(Optional.of(bookDto));
		
		mockMvc.perform(put(BookController.BOOK_PATH_ID, bookDto.getId())
			   .accept(MediaType.APPLICATION_JSON)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(bookDto)))
			   .andExpect(status().isNoContent());
		
		// Verify the book is updated correctly by the controller before being transmitted to the service layer
		verify(bookService).updateBookById(eq(bookDto.getId()), bookCaptor.capture());	
		assertThat(bookCaptor.getValue())
		 					 .usingRecursiveComparison()
		 					 .ignoringFields("createdDate", "updateDate")
		 					 .isEqualTo(bookDto);			
	}
	
	/**
	 * Verifies that a PUT request with blank fields for validation-sensitive fields triggers MethodArgumentNotValidException and returns a 400 Bad Request.
	 */
	@Test
	void updateBookWithBlankFields() throws Exception {
		
		BookDTO bookDto = createValidBook();		
		bookDto.setTitle("");
		bookDto.setAuthor("");
		
	    mockMvc.perform(put(BookController.BOOK_PATH_ID, bookDto.getId())
			   .accept(MediaType.APPLICATION_JSON)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(bookDto)))
			   .andExpect(status().isBadRequest())
			   .andDo(print());
	}
	
	/**
	 * Verifies that a PATCH request partially updates target parameters with a 204 No Content.
	 */
	@Test
	void patchBook() throws Exception {
		
		BookDTO originalBook = createValidBook();
		
		Map<String,String> patch = Map.of("title", "New Title");
		
		mockMvc.perform(patch(BookController.BOOK_PATH_ID, originalBook.getId())
			   .accept(MediaType.APPLICATION_JSON)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(patch)))
			   .andExpect(status().isNoContent());
		
		// Verify that target fields are partially updated correctly by the controller before being transmitted to the service layer
		verify(bookService).patchBookById(eq(originalBook.getId()), bookCaptor.capture());		
		assertThat(patch.get("title")).isEqualTo(bookCaptor.getValue());
	}
	
	/**
	 * Verifies a DELETE request targeting a specific ID deletes  the record and yields a 204 No Content status.
	 */
	@Test
	void deleteBook() throws Exception {
		
		BookDTO bookDto = createValidBook();
		
		given(bookService.deleteBookById(any(UUID.class))).willReturn(true);
		
		mockMvc.perform(delete(BookController.BOOK_PATH_ID, bookDto.getId())
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isNoContent());	
	}
	
	/**
	 * Helper method to instantiate a fully-formed, valid BookDTO sample 
	 */
	private BookDTO createValidBook() {
		
		return BookDTO.builder()
				  .id(UUID.randomUUID())
				  .author("john")
				  .title("programming")
				  .isbn("987-3647582698")
				  .publicationYear(2017)
				  .build();
	}
}
