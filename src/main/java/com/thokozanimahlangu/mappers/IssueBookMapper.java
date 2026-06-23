package com.thokozanimahlangu.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thokozanimahlangu.entities.IssueBook;
import com.thokozanimahlangu.models.IssueBookRequestDTO;
import com.thokozanimahlangu.models.IssueBookResponseDTO;

/**
 * Mapper component responsible for converting data transfer objects (DTO) to and from the {@link IssueBook} entity. 
 */
@Mapper(componentModel = "spring")
public interface IssueBookMapper {

	@Mapping(target = "student", ignore = true)
	@Mapping(target = "book", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "issueDate", ignore = true)
	@Mapping(target = "returnDate", ignore = true)
	@Mapping(target = "updateDate", ignore = true)
	@Mapping(target = "status", ignore = true)
	IssueBook requestIssueBookDTOtoIssueBook(IssueBookRequestDTO requestDto);
	
	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "firstName", source = "student.firstName")
	@Mapping(target = "lastName", source = "student.lastName")
	@Mapping(target = "bookId", source = "book.id")
	@Mapping(target = "bookTitle", source = "book.title")
	IssueBookResponseDTO issueBookToIssueBookResponseDTO(IssueBook issueBook);
}
