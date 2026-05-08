package com.thokozanimahlangu.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.thokozanimahlangu.entities.Student;

/**
 * Utility class for building dynamic JPA queries for the Student entity.
 * These methods return a Specification, which Spring Data JPA converts into a SQL WHERE at runtime.
 */

public class StudentSpecification {

		//Case-insensitive search for the student's first name.
		public static Specification<Student> hasFirstName(String firstName) {
	        return (root, query, cb) -> !StringUtils.hasText(firstName) ? null : 
	               cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
	    }
		
		//Case-insensitive search for the student's last name.
		public static Specification<Student> hasLastName(String lastName) {
	        return (root, query, cb) -> !StringUtils.hasText(lastName) ? null : 
	               cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
		}
		
		//Case-insensitive search for the student's email.
		public static Specification<Student> hasEmail(String email) {
	        return (root, query, cb) -> !StringUtils.hasText(email) ? null : 
	        		cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
		}
}
