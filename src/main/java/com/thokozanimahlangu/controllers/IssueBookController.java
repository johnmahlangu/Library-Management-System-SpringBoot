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

import com.thokozanimahlangu.models.IssueBookRequestDTO;
import com.thokozanimahlangu.models.IssueBookResponseDTO;
import com.thokozanimahlangu.services.IssueBookService;

import lombok.RequiredArgsConstructor;

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
	
	@PostMapping(ISSUE_PATH)
	public ResponseEntity<IssueBookResponseDTO> issueBook(@RequestBody @Validated IssueBookRequestDTO request) {
		
		IssueBookResponseDTO savedIssuedBook = issueBookService.saveIssueBook(request);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", ISSUE_PATH + "/" + savedIssuedBook.getId());
		
		return new ResponseEntity<>(savedIssuedBook, headers, HttpStatus.CREATED);
	}
	
	@PatchMapping(RETURN_PATH)
	public ResponseEntity<?> returnBook(@PathVariable("issueId") UUID issueId) {
		
		issueBookService.returnBook(issueId).orElseThrow(NotFoundException::new);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(ISSUE_PATH)
	public List<IssueBookResponseDTO> listIssuedBooks() {
		
		return issueBookService.listIssuedBooks();
	}
	
	@GetMapping(ISSUE_PATH_ID)
	public IssueBookResponseDTO getIssueBookById(@PathVariable("issueId") UUID issueId) {
		
		return issueBookService.getIssueBookById(issueId).orElseThrow(NotFoundException::new);
	}
	
	@GetMapping(ACTIVE_ISSUE_PATH)
	public List<IssueBookResponseDTO> listActiveIssues() {
		
		return issueBookService.listActiveIssues();
	}
	
	@GetMapping(RETURNED_BOOKS_PATH)
	public List<IssueBookResponseDTO> listReturnedBooks() {
		
		return issueBookService.listReturnedBooks();
	}
	
	@GetMapping(STUDENT_ISSUES_PATH)
	public List<IssueBookResponseDTO> getStudentIssues(@PathVariable("studentId") UUID studentId) {
		
		return issueBookService.getStudentIssues(studentId);
	}
	
	@GetMapping(ACTIVE_STUDENT_ISSUES_PATH)
	public List<IssueBookResponseDTO> getActiveStudentIssues(@PathVariable("studentId") UUID studentId) {
		
		return issueBookService.getStudentActiveIssues(studentId);
	}	
}
