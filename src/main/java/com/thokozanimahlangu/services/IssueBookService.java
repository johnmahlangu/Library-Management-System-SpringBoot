package com.thokozanimahlangu.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.thokozanimahlangu.models.IssueBookRequestDTO;
import com.thokozanimahlangu.models.IssueBookResponseDTO;

public interface IssueBookService {

	IssueBookResponseDTO saveIssueBook(IssueBookRequestDTO request);
	
	Optional<IssueBookResponseDTO> returnBook(UUID issueBookId);
	
	List<IssueBookResponseDTO> listIssuedBooks();
	
	Optional<IssueBookResponseDTO> getIssueBookById(UUID issueId);
	
	List<IssueBookResponseDTO> listActiveIssues();
	
	List<IssueBookResponseDTO> listReturnedBooks();
	
	List<IssueBookResponseDTO> getStudentIssues(UUID studentId);
	
	List<IssueBookResponseDTO> getStudentActiveIssues(UUID studentId);
}
