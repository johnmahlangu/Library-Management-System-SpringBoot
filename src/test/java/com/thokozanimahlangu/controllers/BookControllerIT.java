package com.thokozanimahlangu.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
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
	void listBooks() {
		
		List<BookDTO> bookDto = bookController.listBooks(null, null, null, null);
		
		assertThat(bookDto.size()).isEqualTo(3);
	}
	@Rollback
	@Transactional
	@Test
	void emptyList() {
						
		bookRepository.deleteAll();
		
		List<BookDTO> bookDto = bookController.listBooks(null, null, null, null);
		
		assertThat(bookDto.size()).isEqualTo(0);		
	}
	@Test
	void getBookById() {
		
		Book book = bookRepository.findAll().get(0);
		BookDTO bookDto = bookController.getBook(book.getId());
		
		assertThat(bookDto).isNotNull();
		
	}
}