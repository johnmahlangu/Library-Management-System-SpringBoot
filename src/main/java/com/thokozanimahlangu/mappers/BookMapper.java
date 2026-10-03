package com.thokozanimahlangu.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thokozanimahlangu.entities.Book;
import com.thokozanimahlangu.models.BookDTO;

/**
 * Mapper interface used to convert between Book entities and BookDTOs.
 */
@Mapper(componentModel = "spring")

public interface BookMapper {

	/**
     * Converts a Book entity to a Data Transfer Object(BookDTO).
     * Used when sending book data from the database to the client.
     */
	BookDTO bookToBookDTO(Book book);
	
	/**
     * Converts a BookDTO back into a Book entity.
     * Used when receiving data from the client to be saved or updated in the database.
     */
	@Mapping(target = "issuedBooks", ignore = true)
	Book bookDTOtoBook(BookDTO bookDto);
	
}
