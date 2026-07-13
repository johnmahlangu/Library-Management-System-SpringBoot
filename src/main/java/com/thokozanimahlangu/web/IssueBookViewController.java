package com.thokozanimahlangu.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thokozanimahlangu.services.IssueBookService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IssueBookViewController {

	public static final String ISSUES_PATH = "/issues";
	public static final String ISSUES_VIEW = "issues/list";
	
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
}
