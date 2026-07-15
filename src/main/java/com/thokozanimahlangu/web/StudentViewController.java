package com.thokozanimahlangu.web;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thokozanimahlangu.exceptions.NotFoundException;
import com.thokozanimahlangu.models.StudentDTO;
import com.thokozanimahlangu.services.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentViewController {

	public static final String STUDENTS_PATH = "/students";
	public static final String STUDENTS_REDIRECT = "redirect:/students";
	public static final String STUDENT_PATH_ID = STUDENTS_PATH + "/{studentId}";
	public static final String EDIT_STUDENT_PATH = STUDENTS_PATH + "/edit/{studentId}";
	public static final String EDIT_STUDENT_VIEW = "students/edit";
	public static final String UPDATE_STUDENT_PATH = STUDENTS_PATH + "/update/{studentId}";
	public static final String DELETE_STUDENT_PATH = STUDENTS_PATH + "/delete/{studentId}";
	public static final String STUDENT_LIST_VIEW = "students/list";
	public static final String STUDENT_DETAILS_VIEW = "/students/details";
	public static final String CREATE_STUDENT_PATH = STUDENTS_PATH + "/create";
	public static final String CREATE_STUDENT_VIEW = "students/create";
	
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
	
	/**
	 * Handles GET requests to display the "Create New Student" form.
	 * Initializes an empty StudentDTO to bind form fields.
	 *
	 * @param model the Spring UI Model to pass the empty DTO to the form view
	 * @return the path to the student creation form view template
	 */
	@GetMapping(CREATE_STUDENT_PATH)
	public String createStudentForm(Model model) {
		// Provide an empty DTO to the model
		model.addAttribute("createStudent", new StudentDTO());
		// Render the create student HTML template
		return CREATE_STUDENT_VIEW;
	}
	
	/**
	 * Handles POST requests to process and save a new student submission.;
	 * Validates the input data before persisting it via the service layer.
	 *
	 * @param studentDto the data transfer object containing the submitted form data
	 * @param result  captures any validation errors from the bound object
	 * @return a redirect path on success, or the form view path if validation fails
	 */
	@PostMapping(STUDENTS_PATH)
	public String saveStudent(@Valid @ModelAttribute StudentDTO studentDto, BindingResult result) {
		
		// If validation constraints fail, return to the create student form
		if(result.hasErrors()) {
			return CREATE_STUDENT_VIEW;
		}		
		studentService.saveNewStudent(studentDto);
		// Redirect to prevent duplicate submissions on page refresh
		return STUDENTS_REDIRECT;
	}
	
	/**
	 * Handles GET requests to display the "Edit Student" form populated with the existing student's current details.
	 *
	 * @param studentId 			the unique identifier (UUID) of the student to edit
	 * @param model  				the Spring UI Model to pass the student data to the form
	 * @throws NotFoundException 	if no student matches the provided UUID
	 * @return 	the path to the student editing form view template	 
	 */
	@GetMapping(EDIT_STUDENT_PATH)
	public String editStudentForm(@PathVariable UUID studentId, Model model) {
		
		StudentDTO studentDto = studentService.getStudentByID(studentId).orElseThrow(NotFoundException::new);		
		model.addAttribute("editStudent", studentDto);
		// Render the edit book HTML template
		return EDIT_STUDENT_VIEW;
	}
	
	/**
	 * Handles POST requests to process and save updates to an existing student.
	 * Validates the incoming form data before updating.
	 *
	 * @param studentId   		the unique identifier (UUID) of the student being updated
	 * @param studentDto  		the data transfer object containing the modified student data
	 * @param result   			holds the results of the validation check
	 * @return the edit view if validation fails, or a redirect string if successful
	 */
	@PostMapping(UPDATE_STUDENT_PATH)
	public String updateStudent(@PathVariable UUID studentId, @ModelAttribute StudentDTO studentDto, BindingResult result) {
		// If validation constraints fail, return to the edit form
		if(result.hasErrors()) {
			return EDIT_STUDENT_VIEW;
		}
		studentService.updateStudentById(studentId, studentDto);
		// Redirect to prevent duplicate updates on page refresh
		return STUDENTS_REDIRECT;
		}
	
	/**
	 * Handles POST requests to delete a specific student by ID.
	 *
	 * @param studentId the unique identifier (UUID) of the student to delete
	 * @return a redirect string to the students list page upon successful deletion
	 * @throws NotFoundException if the student to delete does not exist
	 */
	@PostMapping(DELETE_STUDENT_PATH)
	public String deleteStudent(@PathVariable UUID studentId) {
		// Attempt to delete the student; if the service returns false (not found), throw a 404
		if(!studentService.deleteStudentById(studentId)) {
			throw new NotFoundException();
		}
		// Redirect to prevent duplicate deletions on page refresh
		return STUDENTS_REDIRECT;
	}
}