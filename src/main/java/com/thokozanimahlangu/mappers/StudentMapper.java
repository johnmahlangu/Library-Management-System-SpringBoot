package com.thokozanimahlangu.mappers;

import org.mapstruct.Mapper;

import com.thokozanimahlangu.entities.Student;
import com.thokozanimahlangu.models.StudentDTO;

/**
 * Mapper interface used to convert between Student entities and StudentDTOs.
 */
@Mapper
public interface StudentMapper {

	/**
	 * Converts a Student entity to a Data Transfer Object(StudentDTO).
	 * Used when sending student data from the database to the client.
	 */
	StudentDTO studentToStudentDTO(Student student);
	
	/**
	 * Converts a StudentDTO back to a Student entity.
	 * Used when receiving data from the client to be saved or updated in the database.
	 */
	Student studentDTOtoStudent(StudentDTO studentDto);
}
