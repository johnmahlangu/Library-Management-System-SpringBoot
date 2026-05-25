package com.thokozanimahlangu.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.thokozanimahlangu.models.StudentDTO;
import com.thokozanimahlangu.services.StudentService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(StudentController.class)
@ExtendWith(MockitoExtension.class)

/**
 * Slice test for the Student Controller.
 * 
 * This class tests the web layer in isolation by verifying HTTP requests, status codes, payload validation, and JSON serialization.
 */
class StudentControllerTest {

	@Autowired
	MockMvc mockMvc; // Simulates HTTP requests without starting a real server
	
	@Autowired
	ObjectMapper objectMapper; // Handles Java-to-JSON serialization for request bodies
	
	@MockitoBean
	StudentService studentService; // Mocked service layer
	
	@Captor
	ArgumentCaptor<StudentDTO> studentCaptor; // Captures payloads passed to service methods
	
	/**
	 * Verifies that a valid UUID returns the matching student details with a 200 OK status.
	 */
	@Test
	void getStudentById() throws Exception{
		
		StudentDTO studentDto = createValidStudent();
		UUID studentId = studentDto.getId();
		
		given(studentService.getStudentByID(any(UUID.class))).willReturn(Optional.of(studentDto));
		
		mockMvc.perform(get(StudentController.STUDENT_PATH_ID, studentId)
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.id", is(studentId.toString())))
			   .andExpect(jsonPath("$.firstName", is(studentDto.getFirstName())))
			   .andExpect(jsonPath("$.lastName", is(studentDto.getLastName())))
			   .andExpect(jsonPath("$.email", is(studentDto.getEmail())));					   		   
	}
	
	/**
	 * Verifies that searching for a non-existent student ID correctly yields a 404 Not Found status.
	 */
	@Test
	void getStudentByIdNotFound() throws Exception {
		
		given(studentService.getStudentByID(any(UUID.class))).willReturn(Optional.empty());
		
		mockMvc.perform(get(StudentController.STUDENT_PATH_ID, UUID.randomUUID()))
			   .andExpect(status().isNotFound());
	}
	
	/**
	 * Verifies that pulling a collection of books returns the expected list and size with a 200 Ok status.
	 */
	@Test
	void listStudents() throws Exception {
		
		given(studentService.listStudents(any(), any(), any())).willReturn(List.of(createValidStudent(), createValidStudent()));
		
		mockMvc.perform(get(StudentController.STUDENT_PATH)
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.length()", is(2)));
	}
	
	/**
	 * Verifies that a valid student creation request returns a 201 Created status,
	 * includes a Location header, and correctly passes data down to the service layer.
	 */
	@Test
	void saveStudent() throws Exception {
		
		StudentDTO studentDto = createValidStudent();
		
		given(studentService.saveNewStudent(any(StudentDTO.class))).willReturn(studentDto);
		
		mockMvc.perform(post(StudentController.STUDENT_PATH)
			   .accept(MediaType.APPLICATION_JSON)			   
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(studentDto)))
			   .andExpect(status().isCreated())
			   .andExpect(header().exists("Location"));
		
		// Verify the controller correctly mapped and passed the created student data to the service layer
		verify(studentService).saveNewStudent(studentCaptor.capture());
		assertThat(studentCaptor.getValue())
								.usingRecursiveComparison()
								.ignoringFields("createdDate", "updateDate")
								.isEqualTo(studentDto);
	}
	
	/**
	 * Verifies that a POST request with missing required fields triggers MethodArgumentNotValidException and returns a 400 Bad Request.
	 */
	@Test
	void saveStudentWithMissingRequiredFields() throws Exception {
		
		StudentDTO studentDto = StudentDTO.builder().build();
		
		mockMvc.perform(post(StudentController.STUDENT_PATH)
				   .accept(MediaType.APPLICATION_JSON)			   
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(studentDto)))
				   .andExpect(status().isBadRequest())
				   .andDo(print());
	}
	
	/**
	 * Verifies that updating an existing student returns a 204 No Content status
	 */
	@Test
	void updateStudent() throws Exception {
		
		StudentDTO studentDto = createValidStudent();
		UUID studentId = studentDto.getId();
		
		given(studentService.updateStudentById(any(UUID.class), any(StudentDTO.class))).willReturn(Optional.of(studentDto));
		
		mockMvc.perform(put(StudentController.STUDENT_PATH_ID, studentId)
			   .accept(MediaType.APPLICATION_JSON)
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(objectMapper.writeValueAsString(studentDto)))
			   .andExpect(status().isNoContent());
		
		// Verify the controller correctly mapped and passed the created student data to the service layer
		verify(studentService).updateStudentById(eq(studentId), studentCaptor.capture());
		assertThat(studentCaptor.getValue())
								.usingRecursiveComparison()
								.ignoringFields("createdDate", "updateDate")
								.isEqualTo(studentDto);
	}
	
	/**
	 * Verifies that a PUT request with blank fields for validation-sensitive fields triggers MethodArgumentNotValidException and returns a 400 Bad Request.
	 */
	@Test
	void updateStudentWithBlankFields() throws Exception {
		
		StudentDTO studentDto = createValidStudent();
		UUID studentId = studentDto.getId();		
		studentDto.setFirstName("");
		
		mockMvc.perform(put(StudentController.STUDENT_PATH_ID, studentId)
				   .accept(MediaType.APPLICATION_JSON)
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(studentDto)))
				   .andExpect(status().isBadRequest())
				   .andDo(print());
	}
	
	/**
	 * Verifies that a PATCH request partially updates target parameters with a 204 No Content.
	 */
	@Test
	void patchStudent() throws Exception {
		
		StudentDTO originalStudent = createValidStudent();
		UUID originalStudentId = originalStudent.getId();
		
		Map<String, String> patchedStudent = Map.of("firstName", "New Name");
		
		given(studentService.patchStudentById(any(UUID.class), any(StudentDTO.class))).willReturn(Optional.of(originalStudent));
		
		mockMvc.perform(patch(StudentController.STUDENT_PATH_ID, originalStudentId)
				   .accept(MediaType.APPLICATION_JSON)
				   .contentType(MediaType.APPLICATION_JSON)
				   .content(objectMapper.writeValueAsString(patchedStudent)))
				   .andExpect(status().isNoContent());
		
		verify(studentService).patchStudentById(eq(originalStudentId), studentCaptor.capture());
		assertThat(patchedStudent.get("firstName")).isEqualTo(studentCaptor.getValue().getFirstName());		
	}
	
	/**
	 * Verifies a DELETE request targeting a specific ID deletes  the record and yields a 204 No Content status.
	 */
	@Test
	void deleteStudent() throws Exception {
		
		StudentDTO studentDto = createValidStudent();
		UUID studentId = studentDto.getId();
		
		given(studentService.deleteStudentById(any(UUID.class))).willReturn(true);
		
		mockMvc.perform(delete(StudentController.STUDENT_PATH_ID, studentId)
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isNoContent());
	}
	
	/**
	 * Helper method to instantiate a fully-formed, valid StudentDTO sample 
	 */
	private StudentDTO createValidStudent() {
		
		return StudentDTO.builder()
						 .id(UUID.randomUUID())
						 .firstName("Thokozani")
						 .lastName("Mahlangu")
						 .email("tk@gmail.com")
						 .build();
	}
}
