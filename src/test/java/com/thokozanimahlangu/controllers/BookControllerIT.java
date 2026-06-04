package com.thokozanimahlangu.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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

@SpringBootTest
@AutoConfigureMockMvc
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
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	}
	@Test
	void listBooks() throws Exception{
		
		long databaseBookCount = bookRepository.count();
		
		mockMvc.perform(get(BookController.BOOK_PATH)
				   .accept(MediaType.APPLICATION_JSON))
				   .andExpect(status().isOk())
				   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				   .andExpect(jsonPath("$.length()", is((int)databaseBookCount)));
	}
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
	@Test
	void bookIdNotFound() {		
		assertThrows(NotFoundException.class, () -> {
			bookController.getBook(UUID.randomUUID());
		});
	}
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
		
		String headerLocation = result.getResponse().getHeader("Location");
		String[] UUIDlocation = headerLocation.split("/");
		UUID savedUUID = UUID.fromString(UUIDlocation[4]);
		
		Book book = bookRepository.findById(savedUUID).orElse(null);
		assertThat(book).isNotNull();
		assertThat(book.getAuthor()).isEqualTo(bookDto.getAuthor());
		assertThat(book.getTitle()).isEqualTo(bookDto.getTitle());
		assertThat(book.getIsbn()).isEqualTo(bookDto.getIsbn());
		assertThat(book.getPublicationYear()).isEqualTo(bookDto.getPublicationYear());
	}
	private BookDTO createValidBook() {
		return BookDTO.builder()
				      .author("john")
				      .title("programming")
				      .isbn("987-3647582698")
				      .publicationYear(2017)
				      .build();
	}
}