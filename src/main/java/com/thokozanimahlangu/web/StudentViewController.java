package com.thokozanimahlangu.web;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.thokozanimahlangu.exceptions.NotFoundException;
import com.thokozanimahlangu.models.StudentDTO;
import com.thokozanimahlangu.services.StudentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentViewController {

	public static final String STUDENTS_PATH = "/students";
	public static final String STUDENT_PATH_ID = STUDENTS_PATH + "/{studentId}";
	public static final String STUDENT_LIST_VIEW = "students/list";
	public static final String STUDENT_DETAILS_VIEW = "/students/details";
	
	
	private final StudentService studentService;
	
	/**
	 * Handles GET requests to list students. Supports optional filtering by first name, last name and email
	 *
	 * @param firstName           	optional filter for student's first name
	 * @param lastName         		optional filter for student's last name
	 * @param email			 		optional filter for student's email
	 * 
	 * @return the path to the student list view template
	 */
	@GetMapping(STUDENTS_PATH)
	public String listStudents(@RequestParam(required = false) String firstName,
							   @RequestParam(required = false) String lastName,
							   @RequestParam(required = false) String email,
							   Model model) {
		// Fetch filtered students from the service layer and add them to the UI model
		model.addAttribute("listStudents", studentService.listStudents(firstName, lastName, email));
		// Render the student list HTML template
		return STUDENT_LIST_VIEW;
	}
	
	/**
	 * Handles GET requests to retrieve and display details for a specific student.
	 *
	 * @param studentId 			the unique identifier (UUID) of the student to retrieve
	 * @param model  				the Spring UI Model to pass data to the view
	 * @throws NotFoundException 	if no student matches the provided UUID
	 * @return the path to the student details view template
	 */
	@GetMapping(STUDENT_PATH_ID)		
	public String getStudent(@PathVariable UUID studentId, Model model) {
		
		// Fetch student by ID; if the student does not exist, throw a NotFoundException
		StudentDTO student = studentService.getStudentByID(studentId).orElseThrow(NotFoundException::new);
		// Add existing book to the UI model;
		model.addAttribute("student", student);
		// Render the student details HTML template
		return STUDENT_DETAILS_VIEW;
	}
}
