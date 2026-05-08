package com.thokozanimahlangu.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.thokozanimahlangu.entities.Student;

/**
 * Repository interface for Student entity.
 * Provides automated query execution for searching students based on various attributes.
 */

public interface StudentRepository extends JpaRepository<Student, UUID>, JpaSpecificationExecutor<Student>{

		
}
