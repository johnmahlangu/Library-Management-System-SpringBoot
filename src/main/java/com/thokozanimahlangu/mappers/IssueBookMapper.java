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

	/**
	 * Converts an IssueBookRequestDTO into an IssueBook entity.
	 * Some fields are ignored because they are set manually in the service layer.
	 *
	 * @param requestDto request object from client
	 * @return IssueBook entity
	 */
	@Mapping(target = "student", ignore = true)
	@Mapping(target = "book", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "issueDate", ignore = true)
	@Mapping(target = "returnDate", ignore = true)
	@Mapping(target = "updateDate", ignore = true)
	@Mapping(target = "status", ignore = true)
	IssueBook requestIssueBookDTOtoIssueBook(IssueBookRequestDTO requestDto);
	
	/**
	 * Converts an IssueBook entity into a response DTO.
	 * Includes flattened Student and Book details.
	 *
	 * @param issueBook issue entity
	 * @return response DTO
	 */
	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "firstName", source = "student.firstName")
	@Mapping(target = "lastName", source = "student.lastName")
	@Mapping(target = "bookId", source = "book.id")
	@Mapping(target = "bookTitle", source = "book.title")
	@Mapping(target = "email", source = "student.email")
	IssueBookResponseDTO issueBookToIssueBookResponseDTO(IssueBook issueBook);
}
