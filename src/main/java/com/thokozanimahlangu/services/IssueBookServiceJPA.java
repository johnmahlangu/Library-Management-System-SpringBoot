package com.thokozanimahlangu.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.thokozanimahlangu.entities.Book;
import com.thokozanimahlangu.entities.IssueBook;
import com.thokozanimahlangu.entities.Student;
import com.thokozanimahlangu.exceptions.BookAlreadyIssuedException;
import com.thokozanimahlangu.exceptions.BookAlreadyReturnedException;
import com.thokozanimahlangu.exceptions.NotFoundException;
import com.thokozanimahlangu.mappers.IssueBookMapper;
import com.thokozanimahlangu.models.IssueBookRequestDTO;
import com.thokozanimahlangu.models.IssueBookResponseDTO;
import com.thokozanimahlangu.models.IssueStatus;
import com.thokozanimahlangu.repositories.BookRepository;
import com.thokozanimahlangu.repositories.IssueBookRepository;
import com.thokozanimahlangu.repositories.StudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueBookServiceJPA implements IssueBookService{

	private final IssueBookRepository issueBookRepository;
	private final StudentRepository studentRepository;
	private final BookRepository bookRepository;
	private final IssueBookMapper issueBookMapper;
		
	@Transactional
	public IssueBookResponseDTO saveIssueBook(IssueBookRequestDTO request) {
		
		issueBookRepository.findByBookIdAndReturnDateIsNull(request.getBookId())
						   .ifPresent(issue -> { 
							   throw new BookAlreadyIssuedException();
						   });
						   
		Student student = studentRepository.findById(request.getStudentId()).orElseThrow(NotFoundException::new);
			
		Book book = bookRepository.findById(request.getBookId()).orElseThrow(NotFoundException::new);
		
		book.setAvailable(false);
		
		IssueBook issueBook = IssueBook.builder()
									   .student(student)
									   .book(book)
									   .status(IssueStatus.ISSUED)
									   .dueDate(request.getDueDate())
									   .build();
		
		if (issueBook.getStatus() == IssueStatus.RETURNED) {
			throw new BookAlreadyReturnedException();
		}
		student.addStudentIssuedBook(issueBook);
		book.addIssuedBook(issueBook);
			
		return issueBookMapper.issueBookToIssueBookResponseDTO(issueBookRepository.save(issueBook));
	}
	@Transactional
	public IssueBookResponseDTO returnBook(UUID issueBookId) {
		
		IssueBook issueBook = issueBookRepository.findById(issueBookId).orElseThrow(NotFoundException::new);
		
		Book book = issueBook.getBook();
		book.setAvailable(true);
		
		issueBook.setStatus(IssueStatus.RETURNED);
		issueBook.setReturnDate(LocalDate.now());
		
		return issueBookMapper.issueBookToIssueBookResponseDTO(issueBookRepository.save(issueBook));
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
	
	public List<IssueBookResponseDTO> listActiveIssues() {
		
		return issueBookRepository.findByReturnDateIsNull()
				                  .stream()
				                  .map(activeIssue -> issueBookMapper.issueBookToIssueBookResponseDTO(activeIssue))
				                  .collect(Collectors.toList());
	}
	
	public List<IssueBookResponseDTO> listReturnedBooks() {
		
		return issueBookRepository.findByReturnDateIsNull()
								  .stream()
								  .map(returnedBook -> issueBookMapper.issueBookToIssueBookResponseDTO(returnedBook))
								  .collect(Collectors.toList());
	}
	
	public List<IssueBookResponseDTO> getStudentIssues(UUID studentId) {
		
		studentRepository.findById(studentId).orElseThrow(NotFoundException::new);
		
		return issueBookRepository.findByStudentId(studentId)
								  .stream()
								  .map(studentIssue -> issueBookMapper.issueBookToIssueBookResponseDTO(studentIssue))
								  .collect(Collectors.toList());
	}
	
	public List<IssueBookResponseDTO> getStudentActiveIssues(UUID studentId) {
		
		studentRepository.findById(studentId).orElseThrow(NotFoundException::new);
		
		return issueBookRepository.findByStudentIdAndReturnDateIsNull(studentId)
								  .stream()
								  .map(studentActiveIssue -> issueBookMapper.issueBookToIssueBookResponseDTO(studentActiveIssue))
								  .collect(Collectors.toList());		
	}
}
