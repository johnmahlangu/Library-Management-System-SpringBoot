package com.thokozanimahlangu.web;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.thokozanimahlangu.exceptions.NotFoundException;
import com.thokozanimahlangu.models.IssueBookResponseDTO;
import com.thokozanimahlangu.services.IssueBookService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IssueBookViewController {

	public static final String ISSUES_PATH = "/issues";
	public static final String ISSUES_VIEW = "issues/list";
	public static final String ISSUE_PATH_ID = ISSUES_PATH + "/{issueId}";
	public static final String ISSUE_RECORD_VIEW = "issues/records";
	
	private final IssueBookService issueBookService;
	
	/**
	 * Handles Get Requests to list all issue book records(active and history records).
	 *  
	 * @param model		Spring UI model to pass data to view
	 * @return path to the issues record view template
	 */
	@GetMapping(ISSUES_PATH)
	public String listIssues(Model model) {
		// Fetch all issue records via the service layer and add them to the model
		model.addAttribute("listIssues", issueBookService.listIssuedBooks());
		// Render the issue records template	
		return ISSUES_VIEW;	
	}
	
	/**
	 * Handles Get requests for a specific issue details(returned and active records).
	 * 
	 * @param issueId				unique identifier of the issue record.
	 * @param model					the UI model to pass date to view
	 * @param result				captures validation errors 
	 * @throws NotFoundException 	if the provided student ID is not found
	 * @return	the path to the issue record view template
	 */
	@GetMapping(ISSUE_PATH_ID)
	public String getIssueRecord(@PathVariable UUID issueId, Model model, BindingResult result) {
		
		IssueBookResponseDTO issue = issueBookService.getIssueBookById(issueId).orElseThrow(NotFoundException::new);
		// add the issue record to the model
		model.addAttribute("issueRecords", issue);
		// Render the issue record HTML template
		return ISSUE_RECORD_VIEW;
	}
}
