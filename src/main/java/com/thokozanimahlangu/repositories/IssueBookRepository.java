package com.thokozanimahlangu.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thokozanimahlangu.entities.IssueBook;

/**
 * Repository interface for IssueBook entity.
 * Provides abstraction for database operations using Spring Data JPA.
 */

public interface IssueBookRepository extends JpaRepository<IssueBook,UUID>{

}
