package com.thokozanimahlangu.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.thokozanimahlangu.repositories.BookRepository;
import com.thokozanimahlangu.repositories.StudentRepository;

/**
 * Data JPA slice test for verifying that the database is initialized if it is empty.
 * 
 * @DataJpaTest ensures isolated test that configures only repositories and in-memory database H2.
 */
@DataJpaTest
class BootStrapDataTest {

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	BootStrapData bootStrapData;
	
	// Executes before any individual tests executes.
	@BeforeEach
	void setUp() throws Exception {
		
		bootStrapData = new BootStrapData(bookRepository, studentRepository);
	}

	// Tests to verify whether database is initialized with 3 books and 3 students if it is empty.
	@Test
	void test() throws Exception{
		
		bootStrapData.run("");
		
		assertThat(bookRepository.count()).isEqualTo(3);
		assertThat(studentRepository.count()).isEqualTo(3);		
	}
}
