package com.thokozanimahlangu.bootstrap;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.thokozanimahlangu.entities.Book;
import com.thokozanimahlangu.entities.Student;
import com.thokozanimahlangu.repositories.BookRepository;
import com.thokozanimahlangu.repositories.StudentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

/**
 * This class initializes a database.
 * 
 * Its primary purpose is to check if the database is empty, and if so, populate it with default sample data (Books and Students).
 */
public class BootStrapData implements CommandLineRunner{

	private final BookRepository bookRepository;
	private final StudentRepository studentRepository;
	
	/**
	 * Spring Boot calls this method automatically.
	 */
	@Override
	public void run(String... args) throws Exception {
		
		loadBookData();
		loadStudentData();		
	}
	/**
	 * Checks if the student table is empty and populates it with initial sample students.
	 */
	private void loadBookData() {
		
		if(bookRepository.count() == 0) {
			
			Book book1 = Book.builder()
						 .title("Head First Java")
						 .author("Kathy Sierra and Bert Bates")
						 .publicationYear(2003)
						 .isbn("978-0596004651")
						 .createdDate(LocalDateTime.now())
						 .updateDate(LocalDateTime.now())
						 .build();
		
						 		
			Book book2 = Book.builder()
						 .title("Effective Java")
						 .author("Joshua Bloch")
						 .publicationYear(2017)
						 .isbn("978-0134685991")
						 .createdDate(LocalDateTime.now())
						 .updateDate(LocalDateTime.now())
						 .build();
						 
			Book book3 = Book.builder()
						 .title("Java Concurrency in Practice")
						 .author("Brian Goets")
						 .publicationYear(2006)
						 .isbn("978-0321349606")
						 .createdDate(LocalDateTime.now())
						 .updateDate(LocalDateTime.now())
						 .build();
						 		
			bookRepository.saveAll(Arrays.asList(book1, book2, book3));
							
		}
	}
	/**
	 * Checks if the book table is empty and populates it with initial sample books.
	 */
	private void loadStudentData() {
		
		if(studentRepository.count() == 0) {
			
			Student student1 = Student.builder()
							   .firstName("Thokozani")
							   .lastName("Mahlangu")
							   .email("thokozanimahlangu0608@gmail.com")
							   .createdDate(LocalDateTime.now())
							   .updateDate(LocalDateTime.now())
							   .build();
			
			Student student2 = Student.builder()
					   		   .firstName("Paul")
					   		   .lastName("Mthombeni")
					   		   .email("paul@gmail.com")
					   		   .createdDate(LocalDateTime.now())
					   		   .updateDate(LocalDateTime.now())
					   		   .build();
							   		
			Student student3 = Student.builder()
			   		   		   .firstName("John")
			   		   		   .lastName("Smith")
			   		   		   .email("johnsmith@gmail.com")
			   		   		   .createdDate(LocalDateTime.now())
			   		   		   .updateDate(LocalDateTime.now())
			   		   		   .build();
			
			studentRepository.saveAll(Arrays.asList(student1, student2, student3));
		}
	}
}
