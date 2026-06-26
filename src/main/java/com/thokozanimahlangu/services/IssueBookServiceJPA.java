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

/**
 * Service implementation responsible for business logic
 * related to issuing and returning books.
 *
 * Handles:
 * - Validating book availability
 * - Creating issue records
 * - Updating return records
 * - Fetching issue history
 */
public class IssueBookServiceJPA implements IssueBookService{

	private final IssueBookRepository issueBookRepository;
	private final StudentRepository studentRepository;
	private final BookRepository bookRepository;
	private final IssueBookMapper issueBookMapper;
	
	/**
	 * Creates a new issue record for a student borrowing a book.
	 * 
	 * Updates book availability and marks the issue as issued.
	 * 
	 * @param request issue request data
	 * @return saved issue record
	 * @throws BookAlreadyIssuedException if book is already issued
	 * @throws NotFoundException if student or book is not found
	 */
	@Transactional
	public IssueBookResponseDTO saveIssueBook(IssueBookRequestDTO request) {
		
		issueBookRepository.findByBookIdAndReturnDateIsNull(request.getBookId())
						   .ifPresent(_ -> { 
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
	
	/**
	 * Processes a book return.
	 *
	 * Updates book availability and marks the issue as returned.
	 * 
	 * @param issueBookId issue transaction ID
	 * @return updated issue record
	 * @throws NotFoundException if issue record does not exist
	 */
	@Transactional
	public IssueBookResponseDTO returnBook(UUID issueBookId) {
		
		IssueBook issueBook = issueBookRepository.findById(issueBookId).orElseThrow(NotFoundException::new);
		
		Book book = issueBook.getBook();
		book.setAvailable(true);
		
		issueBook.setStatus(IssueStatus.RETURNED);
		issueBook.setReturnDate(LocalDate.now());
		
		return issueBookMapper.issueBookToIssueBookResponseDTO(issueBookRepository.save(issueBook));
	}
	
	/**
	 * Retrieves all issue records.
	 *
	 * @return list of all issued books
	 */
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
	
	/**
	 * Retrieves only active (not returned) issue records.
	 *
	 * @return list of active issues
	 */
	public List<IssueBookResponseDTO> listActiveIssues() {
		
		return issueBookRepository.findByReturnDateIsNull()
				                  .stream()
				                  .map(activeIssue -> issueBookMapper.issueBookToIssueBookResponseDTO(activeIssue))
				                  .collect(Collectors.toList());
	}
	
	/**
	 * Retrieves all returned book records.
	 *
	 * @return list of returned books
	 */
	public List<IssueBookResponseDTO> listReturnedBooks() {
		
		return issueBookRepository.findByReturnDateIsNull()
								  .stream()
								  .map(returnedBook -> issueBookMapper.issueBookToIssueBookResponseDTO(returnedBook))
								  .collect(Collectors.toList());
	}
	
	/**
	 * Retrieves all books borrowed to the student(current and past issues).
	 * 
	 * @param student ID
	 * @throws NotFoundException if student does not exist
	 * @return list of books borrowed to the student(current and past issues)
	 */
	public List<IssueBookResponseDTO> getStudentIssues(UUID studentId) {
		
		studentRepository.findById(studentId).orElseThrow(NotFoundException::new);
		
		return issueBookRepository.findByStudentId(studentId)
								  .stream()
								  .map(studentIssue -> issueBookMapper.issueBookToIssueBookResponseDTO(studentIssue))
								  .collect(Collectors.toList());
	}
	
	/**
	 * Retrieves all books currently borrowed to the student.
	 * 
	 * @param student ID
	 * @throws NotFoundException if student is not found
	 * @return list of books currently borrowed to the student
	 */
	public List<IssueBookResponseDTO> getStudentActiveIssues(UUID studentId) {
		
		studentRepository.findById(studentId).orElseThrow(NotFoundException::new);
		
		return issueBookRepository.findByStudentIdAndReturnDateIsNull(studentId)
								  .stream()
								  .map(studentActiveIssue -> issueBookMapper.issueBookToIssueBookResponseDTO(studentActiveIssue))
								  .collect(Collectors.toList());		
	}
}
