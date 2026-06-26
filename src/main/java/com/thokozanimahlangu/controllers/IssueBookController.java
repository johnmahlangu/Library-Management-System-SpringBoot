package com.thokozanimahlangu.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.thokozanimahlangu.exceptions.NotFoundException;
import com.thokozanimahlangu.models.IssueBookRequestDTO;
import com.thokozanimahlangu.models.IssueBookResponseDTO;
import com.thokozanimahlangu.services.IssueBookService;

import lombok.RequiredArgsConstructor;

/**
 * REST Controller responsible for managing book issue and return operations.
 * 
 * Exposes endpoints for:
 * - Issuing books to students
 * - Returning books
 * - Retrieving issue records
 * - Filtering active and returned books
 * - Viewing issue history per student
 */
@RestController
@RequiredArgsConstructor
public class IssueBookController {

	public static final String ISSUE_PATH = "/api/v1/issues";
	
	public static final String ISSUE_PATH_ID = ISSUE_PATH + "/{issueId}";
	
	public static final String RETURN_PATH = ISSUE_PATH + "/{issueId}/return";
	
	public static final String ACTIVE_ISSUE_PATH = ISSUE_PATH + "/active";
	
	public static final String RETURNED_BOOKS_PATH = ISSUE_PATH + "/returned";
	
	public static final String STUDENT_ISSUES_PATH = StudentController.STUDENT_PATH + "/{studentId}/issues";
	
	public static final String ACTIVE_STUDENT_ISSUES_PATH = STUDENT_ISSUES_PATH + "/active";
	
	private final IssueBookService issueBookService;
	
	/**
	 * Issues a book to a student.
	 *
	 * @param request contains student ID, book ID, and due date
	 * @return the created issue record with HTTP 201
	 */
	@PostMapping(ISSUE_PATH)
	public ResponseEntity<IssueBookResponseDTO> issueBook(@RequestBody @Validated IssueBookRequestDTO request) {
		
		IssueBookResponseDTO savedIssuedBook = issueBookService.saveIssueBook(request);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", ISSUE_PATH + "/" + savedIssuedBook.getId());
		
		return new ResponseEntity<>(savedIssuedBook, headers, HttpStatus.CREATED);
	}
	
	/**
	 * Marks an issued book as returned.
	 *
	 * @param issueId unique ID of the issue record
	 * @return HTTP 204 when successful
	 */
	@PatchMapping(RETURN_PATH)
	public ResponseEntity<?> returnBook(@PathVariable("issueId") UUID issueId) {
		
		issueBookService.returnBook(issueId);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Retrieves all issued book records.
	 *
	 * @return list of all issue records
	 */
	@GetMapping(ISSUE_PATH)
	public List<IssueBookResponseDTO> listIssuedBooks() {
		
		return issueBookService.listIssuedBooks();
	}
	
	/**
	 * Retrieves a specific issue record by ID.
	 *
	 * @param issueId unique issue ID
	 * @return matching issue record
	 * @throws NotFoundException if issue does not exist
	 */
	@GetMapping(ISSUE_PATH_ID)
	public IssueBookResponseDTO getIssueBookById(@PathVariable("issueId") UUID issueId) {
		
		return issueBookService.getIssueBookById(issueId).orElseThrow(NotFoundException::new);
	}
	
	/**
	 * Retrieves all active book issues
	 * 
	 * @return all active book issues
	 */
	@GetMapping(ACTIVE_ISSUE_PATH)
	public List<IssueBookResponseDTO> listActiveIssues() {
		
		return issueBookService.listActiveIssues();
	}
	
	/**
	 * Retrieves all returned book
	 *  
	 * @return list of all returned books
	 */
	@GetMapping(RETURNED_BOOKS_PATH)
	public List<IssueBookResponseDTO> listReturnedBooks() {
		
		return issueBookService.listReturnedBooks();
	}
	
	/**
	 * Retrieves all student's issue records.
	 * 
	 * @param student ID
	 * @return list of all student's issue records
	 */
	
	@GetMapping(STUDENT_ISSUES_PATH)
	public List<IssueBookResponseDTO> getStudentIssues(@PathVariable("studentId") UUID studentId) {
		
		return issueBookService.getStudentIssues(studentId);
	}
	
	/**
	 * Retrieves all student's active issues records
	 *   
	 * @param student ID
	 * @return list of all student's active issues records
	 */
	@GetMapping(ACTIVE_STUDENT_ISSUES_PATH)
	public List<IssueBookResponseDTO> getActiveStudentIssues(@PathVariable("studentId") UUID studentId) {
		
		return issueBookService.getStudentActiveIssues(studentId);
	}	
}
