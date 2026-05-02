package com.thokozanimahlangu.mappers;

import org.mapstruct.Mapper;

import com.thokozanimahlangu.entities.Book;
import com.thokozanimahlangu.models.BookDTO;

/**
 * Mapper interface used to convert between Book entities and BookDTOs.
 */

@Mapper
public interface BookMapper {

	/**
     * Converts a Book entity to a Data Transfer Object(BookDTO).
     * Used when sending book data from the database to the client.
     */
	BookDTO booktoBookDTO(Book book);
	
	/**
     * Converts a BookDTO back into a Book entity.
     * Used when receiving data from the client to be saved or updated in the database.
     */
	Book bookDTOtoBook(BookDTO bookDto);
	
}
