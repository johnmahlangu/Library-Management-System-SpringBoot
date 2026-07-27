package com.thokozanimahlangu.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thokozanimahlangu.services.BookService;
import com.thokozanimahlangu.services.IssueBookService;
import com.thokozanimahlangu.services.StudentService;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsible for gathering system metrics that are displayed to the application's homepage dashboard.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final BookService bookService;
	private final StudentService studentService;
	private final IssueBookService issueBookService;
	
	/**
     * Handles the HTTP GET request for the root URL ("/") and prepares the dashboard statistics.
     *
     * @param model the UI Model used to pass data attributes to the view template
     * @return the name of the view template ("index") to render
     */
	@GetMapping("/")
	public String home(Model model) {
		
		// Fetch total count of all books in the system (passing nulls to bypass filtering)
		model.addAttribute("totalBooks", bookService.listBooks(null, null, null, null, null, null).size());
		
		// Fetch total count of all students in the system (passing nulls to bypass filtering)
		model.addAttribute("totalStudents", studentService.listStudents(null, null, null, null).size());
		
		// Fetch the total count of books currently issued/borrowed out
		model.addAttribute("totalActiveIssues", issueBookService.listActiveIssues().size());
		
		// Fetch the total count of books that have been successfully returned
		model.addAttribute("totalReturnedBooks", issueBookService.listReturnedBooks().size());
		
		// Render the index.html Thymeleaf templat
		return "index";
	}
}
