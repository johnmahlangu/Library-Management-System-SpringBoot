package com.thokozanimahlangu.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.thokozanimahlangu.entities.Student;
import com.thokozanimahlangu.mappers.StudentMapper;
import com.thokozanimahlangu.models.StudentDTO;
import com.thokozanimahlangu.repositories.StudentRepository;
import com.thokozanimahlangu.repositories.StudentSpecification;

import lombok.RequiredArgsConstructor;

/**
 * Retrieves a list of students filtered by optional criteria.
 * Uses JPA Specifications for dynamic query building.
 */

@Service
@RequiredArgsConstructor
public class StudentServiceJPA implements StudentService{

	private final StudentMapper studentMapper;
	private final StudentRepository studentRepository;
	
	
	public List<StudentDTO> listStudents(String firstName, String lastName, String email) {
		
		// Build dynamic query criteria based on provided parameters
		Specification<Student> spec = Specification.where(StudentSpecification.hasFirstName(firstName))
												    .and(StudentSpecification.hasLastName(lastName))
												    .and(StudentSpecification.hasEmail(email));
		
		// Fetch entities, map to DTOs, and return as a list
		return studentRepository.findAll(spec)
								.stream()
								.map(student -> studentMapper.studentToStudentDTO(student))
								.collect(Collectors.toList());
	}
	
	/**
     * Finds a single student by their unique UUID.
     * Returns an Optional to handle cases where the ID might not exist.
     */
	public Optional<StudentDTO> getStudentByID(UUID id) {
		
		return studentRepository.findById(id)
								.map(student -> studentMapper.studentToStudentDTO(student));
	}
	
	/**
     * Persists a new student record.
     * Maps DTO to Entity for saving, then converts the result back to DTO.
     */
	public StudentDTO saveNewStudent(StudentDTO student) {
		
		return studentMapper.studentToStudentDTO(studentRepository.save(studentMapper.studentDTOtoStudent(student)));
	}
	
	/**
     * Deletes a student if they exist in the database.
     * @return true if deleted, false if the record was not found.
     */
	public Boolean deleteStudentById(UUID id) {
		
		if (studentRepository.existsById(id)) {
			
			studentRepository.deleteById(id);
			
			return true;
		}
		
		return false;
	}
	
	/**
     * Performs a full update of an existing student.
     * Overwrites the core fields regardless of whether they are null in the DTO.
     */
	public Optional<StudentDTO> updateStudentById(UUID id, StudentDTO studentDto) {
		
		return studentRepository.findById(id)
								.map(foundStudent -> {
									foundStudent.setFirstName(studentDto.getFirstName());
									foundStudent.setLastName(studentDto.getLastName());
									foundStudent.setEmail(studentDto.getEmail());
									
									return studentMapper.studentToStudentDTO(studentRepository.save(foundStudent));
								});
	}
	
	/**
     * Performs a partial update (Patch).
     * Only updates fields that are actually provided (not null/empty) in the DTO.
     */
	public Optional<StudentDTO> patchStudentById(UUID id, StudentDTO studentDto) {
		
		return studentRepository.findById(id)
								.map(foundStudent -> {
									// Only update firstName if text is actually provided
									if (StringUtils.hasText(studentDto.getFirstName()) ) {
										foundStudent.setFirstName(studentDto.getFirstName());						
									}
									// Only update lastName if text is actually provided
									if (StringUtils.hasText(studentDto.getLastName()) ) {
										foundStudent.setLastName(studentDto.getLastName());
									}
									// Only update email if text is actually provided
									if (StringUtils.hasText(studentDto.getEmail())) {
										foundStudent.setEmail(studentDto.getEmail());
									}
									
									return studentMapper.studentToStudentDTO(studentRepository.save(foundStudent));
								});
	}
}

















