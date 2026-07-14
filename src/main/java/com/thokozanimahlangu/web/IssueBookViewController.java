package com.thokozanimahlangu.web;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.thokozanimahlangu.exceptions.NotFoundException;
import com.thokozanimahlangu.models.IssueBookRequestDTO;
import com.thokozanimahlangu.models.IssueBookResponseDTO;
import com.thokozanimahlangu.services.BookService;
import com.thokozanimahlangu.services.IssueBookService;
import com.thokozanimahlangu.services.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IssueBookViewController {

	public static final String ISSUES_PATH = "/issues";
	public static final String ISSUES_VIEW = "issues/list";
	public static final String ISSUE_PATH_ID = ISSUES_PATH + "/{issueId}";
	public static final String ISSUE_RECORD_VIEW = "issues/records";
	public static final String CREATE_ISSUE = ISSUES_PATH + "/create";
	public static final String CREATE_ISSUE_VIEW = "issues/create";
	public static final String ISSUES_PATH_REDIRECT = "redirect:/issues";
	
	private final IssueBookService issueBookService;
	private final StudentService studentService;
	private final BookService bookService;
	
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
	 * @throws NotFoundException 	if the provided student ID is not found
	 * @return	the path to the issue record view template
	 */
	@GetMapping(ISSUE_PATH_ID)
	public String getIssueRecord(@PathVariable UUID issueId, Model model) {
		
		IssueBookResponseDTO issue = issueBookService.getIssueBookById(issueId).orElseThrow(NotFoundException::new);
		// add the issue record to the model
		model.addAttribute("issueRecords", issue);
		// Render the issue record HTML template
		return ISSUE_RECORD_VIEW;
	}
	
	/**
	 * Handles GET requests to display the "Issue Book" form.
	 * Populates the UI model with an empty request DTO and lists of all students and books to populate the form.
	 *
	 * @param model 	the UI model used to pass data to view
	 * @return the path to the book issuing form view template
	 */
	@GetMapping(CREATE_ISSUE)
	public String createIssueForm(Model model) {
		// Bind an empty DTO to back the form fields for the transaction request
		model.addAttribute("issueRequest", new IssueBookRequestDTO());
		// Retrieve all active students (passing nulls to bypass search filters) for selection
		model.addAttribute("students", studentService.listStudents(null, null, null));
		// Retrieve all books (passing nulls to bypass search filters) to populate the book selection
		model.addAttribute("books", bookService.listBooks(null, null, null, null));
		// Render the create issue view template
		return CREATE_ISSUE_VIEW;
	}
	
	/**
	 * Handles POST requests to process and save a new issue record submission.;
	 * Validates the input data before persisting it via the service layer.
	 *
	 * @param request 		the data transfer object containing the submitted form data
	 * @param result  		captures any validation errors from the request object
	 * @return a redirect path on success, or the create issue view path if validation fails
	 */
	@PostMapping(ISSUES_PATH)
	public String saveIssue(@Valid @ModelAttribute IssueBookRequestDTO request, BindingResult result) {
		// If validation constraints fail, return to the create issue form
		if(result.hasErrors()) {
			return CREATE_ISSUE_VIEW;
		}
		// Persist issue record via the service layer
		issueBookService.saveIssueBook(request);
		// Redirect to prevent duplicate submissions on page refresh
		return ISSUES_PATH_REDIRECT;
	}
	
}
