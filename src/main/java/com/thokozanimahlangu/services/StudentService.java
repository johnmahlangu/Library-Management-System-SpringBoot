package com.thokozanimahlangu.services;

import com.thokozanimahlangu.models.StudentDTO;

public interface StudentService {

	Optional<StudentDTO> getStudentByID(UUID id);
}
