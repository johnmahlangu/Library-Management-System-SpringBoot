package com.thokozanimahlangu.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.thokozanimahlangu.entities.Book;

/**
 * Repository interface for Book entity.
 * Provides automated query execution for searching books based on various attributes.
 */

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book>{

}
