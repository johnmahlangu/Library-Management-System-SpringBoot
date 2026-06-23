package com.thokozanimahlangu.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.thokozanimahlangu.controllers.NotFoundException;
import com.thokozanimahlangu.entities.Book;
import com.thokozanimahlangu.entities.IssueBook;
import com.thokozanimahlangu.entities.Student;
import com.thokozanimahlangu.mappers.IssueBookMapper;
import com.thokozanimahlangu.models.IssueBookRequestDTO;
import com.thokozanimahlangu.models.IssueBookResponseDTO;
import com.thokozanimahlangu.models.IssueStatus;
import com.thokozanimahlangu.repositories.BookRepository;
import com.thokozanimahlangu.repositories.IssueBookRepository;
import com.thokozanimahlangu.repositories.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueBookServiceJPA implements IssueBookService{

	private final IssueBookRepository issueBookRepository;
	private final StudentRepository studentRepository;
	private final BookRepository bookRepository;
	private final IssueBookMapper issueBookMapper;
		
	public IssueBookResponseDTO saveIssueBook(IssueBookRequestDTO request) {
			
		Student student = studentRepository.findById(request.getStudentId()).orElseThrow(NotFoundException::new);
			
		Book book = bookRepository.findById(request.getBookId()).orElseThrow(NotFoundException::new);
		
		IssueBook issueBook = IssueBook.builder()
									   .student(student)
									   .book(book)
									   .status(IssueStatus.ISSUED)
									   .dueDate(request.getDueDate())
									   .build();
		
		student.addStudentIssuedBook(issueBook);
		book.addIssuedBook(issueBook);
			
		return issueBookMapper.issueBookToIssueBookResponseDTO(issueBookRepository.save(issueBook));
	}
	
	public Optional<IssueBookResponseDTO> returnBook(UUID issueBookId) {
		
		IssueBook issueBook = issueBookRepository.findById(issueBookId).orElseThrow(NotFoundException::new);
		
		issueBook.setStatus(IssueStatus.RETURNED);
		issueBook.setReturnDate(LocalDate.now());
		
		return Optional.of(issueBookMapper.issueBookToIssueBookResponseDTO(issueBookRepository.save(issueBook)));
	}
	
	public List<IssueBookResponseDTO> listIssuedBooks() {
		
		return issueBookRepository.findAll()
								  .stream()
								  .map(issuedBook -> issueBookMapper.issueBookToIssueBookResponseDTO(issuedBook))
								  .collect(Collectors.toList());
	}
	
	public Optional<IssueBookResponseDTO> getIssueBookById(UUID issueId) {
		
		return issueBookRepository.findById(issueId)
								  .map(issuedBook -> issueBookMapper.issueBookToIssueBookResponseDTO(issuedBook));
	}
}
