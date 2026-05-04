package com.thokozanimahlangu.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.thokozanimahlangu.models.StudentDTO;

/**
 * Service Interface for managing Student operations.
 * Defines the contract for business logic related to StudentDTOs.
 */

public interface StudentService {

	Optional<StudentDTO> getStudentByID(UUID id);
	
	List<StudentDTO> listStudents(String firstName, String lastName, String email);
	
	StudentDTO saveNewStudent(StudentDTO studentDto);
	
	Optional<StudentDTO> updateStudentById(UUID id, StudentDTO studentDto);
	
	Boolean deleteStudentById(UUID id);
	
	Optional<StudentDTO> patchStudentById(UUID id, StudentDTO studentDto);
	
}
