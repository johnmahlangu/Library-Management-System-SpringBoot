package com.thokozanimahlangu.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thokozanimahlangu.entities.Student;

/**
 * Repository interface for Student entity.
 * Provides abstraction for database operations using Spring Data JPA.
 */

public interface StudentRepository extends JpaRepository<Student, UUID>{

	//Searches for students by firstName(case-insensitive) and returns matching students
	List<Student> findAllByFirstNameIsLikeIgnoreCase(String firstName);
	
	//Searches for students by lastName(case-insensitive) and returns matching students
	List<Student> findAllByLastNameIsLikeIgnoreCase(String lastName);
	
	//Searches for students by email(case-insensitive) and returns matching students
	List<Student> findAllByEmailIsLikeIgnoreCase(String email);
	
	//Searches for students by firstName and last name (all case-insensitive) and returns matching students
	List<Student> findAllByFirstNameIsLikeIgnoreCaseAndLastNameIsLikeIgnoreCase(String firstName, String lastName);
	
	//Searches for students by firstName and email (all case-insensitive) and returns matching students
	List<Student> findAllByFirstNameIsLikeIgnoreCaseAndEmailIsLikeIgnoreCase(String firstName, String email);
	
	//Searches for students by last name and email (all case-insensitive) and returns matching students
	List<Student> findAllByLastNameIsLikeIgnoreCaseAndEmailIsLikeIgnoreCase(String firstName, String lastName);
	
	//Searches for students by first name, last name and email (all case-insensitive) and returns matching students
	List<Student> findAllByFirstNameIsLikeIgnoreCaseAndLastNameIsLikeIgnoreCaseAndEmailIsLikeIgnoreCase(String firstName, String lastName, String email);
	
	
}
