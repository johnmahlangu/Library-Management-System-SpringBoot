package com.thokozanimahlangu.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thokozanimahlangu.models.StudentDTO;
import com.thokozanimahlangu.services.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StudentController {

	private static final String STUDENT_PATH = "/api/student";
	private static final String STUDENT_PATH_ID = STUDENT_PATH + "/{studentId}";
	
	private final StudentService studentService;
	
	/**
     * List all students with optional filtering
     * Filters are passed as query parameters
     */
	@GetMapping(STUDENT_PATH)
	public List<StudentDTO> listStudents(@RequestParam(required = false) String firstName,
										 @RequestParam(required = false) String lastName,
										 @RequestParam(required = false) String email) {
		
		return studentService.listStudents(firstName, lastName, email);
	}
	
	/**
     * Retrieve a single student by its UUID
     * Directly returns the DTO, mapping the empty Optional to a 404 exception
     */
	@GetMapping(STUDENT_PATH_ID)
	public StudentDTO getStudent(@PathVariable("studentId") UUID id) {
		
		return studentService.getStudentByID(id).orElseThrow(NotFoundException::new);
	}
	
	/**
     * Create a new student record
     * Returns 201 Created and includes the 'Location' header pointing to the new book
     */
	@PostMapping(STUDENT_PATH)
	public ResponseEntity<?> handlePost(@Validated @RequestBody StudentDTO student) {
		
		StudentDTO newStudent = studentService.saveNewStudent(student);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", STUDENT_PATH + "/" + newStudent.getId());
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	/**
     * Delete a student by ID
     * Throws NotFoundException (404) if the book does not exist
     */
	@DeleteMapping(STUDENT_PATH_ID)
	public ResponseEntity<?> deleteStudent(@PathVariable("studentId") UUID id) {
		
		if (!studentService.deleteStudentById(id)) {
			
			throw new NotFoundException();
		}		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
     * Replace an existing student with new data
     * Uses Optional.isEmpty() to verify existence; throws 404 if missing
     */
	@PutMapping(STUDENT_PATH_ID)
	public ResponseEntity<?> updateStudent(@PathVariable("studentId") UUID id, @Validated @RequestBody StudentDTO student) {
		
		if (studentService.updateStudentById(id, student).isEmpty()) {
			
			throw new NotFoundException();
		}	
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/**
     * Partially update an existing student
     * Validates input and returns 204 No Content upon success
     */
	@PutMapping(STUDENT_PATH_ID)
	public ResponseEntity<?> patchStudent(@PathVariable("studentId") UUID id, @Validated @RequestBody StudentDTO student) {
		
		if (studentService.patchStudentById(id, student).isEmpty()) {
			
			throw new NotFoundException();
		}	
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
