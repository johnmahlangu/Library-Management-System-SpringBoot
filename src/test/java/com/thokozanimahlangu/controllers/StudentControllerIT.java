package com.thokozanimahlangu.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.thokozanimahlangu.entities.Student;
import com.thokozanimahlangu.mappers.StudentMapper;
import com.thokozanimahlangu.models.StudentDTO;
import com.thokozanimahlangu.repositories.StudentRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration Tests for the  StudentController.
 * This class boots up the complete Spring application context (`@SpringBootTest`)  and configures `MockMvc` to test the HTTP end-points without
 * spinning up a real server.
 * It interacts directly with the database to seed data, perform actions via HTTP, and assert state changes.
 */
@SpringBootTest
class StudentControllerIT {

	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	StudentController studentController;
	
	@Autowired
	StudentMapper studentMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	WebApplicationContext webAppContext;
	
	MockMvc mockMvc;
	
	@BeforeEach
	void setUp() {
		// Initialize MockMvc using the full application web context
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
	}

	/**
	 * Tests fetching all students when data exists.
	 * Verifies the response size matches the exact count of records in the database.
	 */
	@Test
	void listStudents() throws Exception {
		
		long databaseStudentCount = studentRepository.count();
		
		mockMvc.perform(get(StudentController.STUDENT_PATH)
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.length()", is((int)databaseStudentCount)));
	}
	
	/**
	 * Tests fetching students when the database record is completely empty.
	 */
	@Transactional
	@Rollback
	@Test
	void emptyList() throws Exception {
		
		studentRepository.deleteAll();
		
		mockMvc.perform(get(StudentController.STUDENT_PATH)
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.length()", is(0)));
	}
	
	/**
	 * Tests retrieving a single student using a valid, existing ID.
	 * Asserts that all returned JSON fields match the database record exactly.
	 */
	@Test
	void getStudentById() throws Exception {
		
		Student student = studentRepository.findAll().get(0);
		UUID studentId = student.getId();
		
		mockMvc.perform(get(StudentController.STUDENT_PATH_ID, studentId)
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.id", is(studentId.toString())))
			   .andExpect(jsonPath("$.firstName", is(student.getFirstName())))
			   .andExpect(jsonPath("$.lastName", is(student.getLastName())))
			   .andExpect(jsonPath("$.email", is(student.getEmail())));
	}
	
	/**
	 * Tests retrieving a student with a non-existent random UUID.
	 * Expects a 404 Not Found response.
	 */
	@Test
	void studentIdNotFound() throws Exception {
		
		mockMvc.perform(get(StudentController.STUDENT_PATH_ID, UUID.randomUUID()))
		       .andExpect(status().isNotFound());
	}
	
	/**
	 * Tests the creation of a new student.
	 * Extracts the new resource path from the 'Location' header to verify database persistence.
	 */
	@Transactional
	@Rollback
	@Test
	void saveNewStudent() throws Exception {
		
		StudentDTO studentDto = createValidStudent();
		
		MvcResult result = mockMvc.perform(post(StudentController.STUDENT_PATH)
			   .accept(MediaType.APPLICATION_JSON)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(studentDto)))
		       .andExpect(status().isCreated())
		       .andExpect(header().exists("Location"))
		       .andReturn();
		
		// Extract the newly generated UUID from the 'Location' header path
		String headerLocation = result.getResponse().getHeader("Location");
		String[] UUIDlocation = headerLocation.split("/");
		UUID savedUUID = UUID.fromString(UUIDlocation[4]);
		
		// Fetch from DB using the extracted UUID and verify fields matched the DTO
		Student student = studentRepository.findById(savedUUID).orElse(null);
		assertThat(student).isNotNull();
		assertThat(student.getFirstName()).isEqualTo(studentDto.getFirstName());
		assertThat(student.getLastName()).isEqualTo(studentDto.getLastName());
		assertThat(student.getEmail()).isEqualTo(studentDto.getEmail());		
	}
	
	/**
	 * Tests a complete update (PUT) of an existing student.
	 * Modifies a field, issues PUT, and verifies the update was saved to the database.
	 */
	@Transactional
	@Rollback
	@Test
	void updateStudent() throws Exception {
		
		Student student = studentRepository.findAll().get(0);
		StudentDTO studentDto = studentMapper.studentToStudentDTO(student);
		// Update a specific field in the payload
		studentDto.setFirstName("Sarah");
		
		mockMvc.perform(put(StudentController.STUDENT_PATH_ID, student.getId())
			   .accept(MediaType.APPLICATION_JSON)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(studentDto)))
		       .andExpect(status().isNoContent());
		
		// Retrieve the entity directly from DB to verify changes stuck
		Student updatedStudent = studentRepository.findAll().get(0);
		assertThat(updatedStudent.getFirstName()).isEqualTo(studentDto.getFirstName());	
	}
	
	/**
	 * Tests updating a student resource that does not exist.
	 * Expects a 404 Not Found response.
	 */
	@Test
	void updateStudentWithInvalidId() throws Exception {
		
		StudentDTO studentDto = createValidStudent();
		
		mockMvc.perform(put(StudentController.STUDENT_PATH_ID, UUID.randomUUID())
				   .accept(MediaType.APPLICATION_JSON)
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(studentDto)))
			       .andExpect(status().isNotFound());
	}
	
	/**
	 * Tests deleting a student.
	 * Verifies that the resource returns 204 NoContent, and is gone from the database.
	 */
	@Transactional
	@Rollback
	@Test
	void deleteStudentById() throws Exception {
		
		Student student = studentRepository.findAll().get(0);
		
		mockMvc.perform(delete(StudentController.STUDENT_PATH_ID, student.getId())
			   .accept(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNoContent());
		
		boolean exists = studentRepository.existsById(student.getId());
		assertFalse(exists);		
	}
	
	/**
	 * Tests deleting a student that does not exist.
	 * Expects a 404 Not Found response.
	 */
	@Test
	void deleteStudentWithInvalidId() throws Exception {
		
		mockMvc.perform(get(StudentController.STUDENT_PATH_ID, UUID.randomUUID()))
		       .andExpect(status().isNotFound());
	}
	
	/**
	 * Tests partial modification (PATCH) of a student.
	 * Sends a partial map containing only the first name, and verifies the update persists.
	 */
	@Transactional
	@Rollback
	@Test
	void patchStudent() throws Exception {
		
		Student student = studentRepository.findAll().get(0);
		Map<String, String> patchStudent = Map.of("firstName", "Thandiwe");
		
		mockMvc.perform(patch(StudentController.STUDENT_PATH_ID, student.getId())
			   .accept(MediaType.APPLICATION_JSON)
		       .contentType(MediaType.APPLICATION_JSON)
		       .content(objectMapper.writeValueAsString(patchStudent)))
		       .andExpect(status().isNoContent());
		
		Student patchedStudent = studentRepository.findAll().get(0);
		assertThat(patchedStudent.getFirstName()).isEqualTo(patchStudent.get("firstName"));				
	}
	
	/**
	 * Tests patching a student that does not exist.
	 * Expects a 404 Not Found response.
	 */
	@Test
	void patchStudentWithInvalidId() throws Exception {
		
		Map<String, String> patchStudent = Map.of("firstName", "Thandiwe");
		
		mockMvc.perform(patch(StudentController.STUDENT_PATH_ID, UUID.randomUUID())
			   .accept(MediaType.APPLICATION_JSON)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(patchStudent)))
		       .andExpect(status().isNotFound());
	}
	
	/**
	 * Helper method to construct a valid StudentDTO for creation payloads.
	 * return a pre-populated StudentDTO instance.
	 */
	private StudentDTO createValidStudent() {		
		return StudentDTO.builder()
						 .firstName("Thokozani")
						 .lastName("Mahlangu")
						 .email("thokozani@gmail.com")
						 .build();
	}
}
