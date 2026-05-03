package com.thokozanimahlangu.services;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.thokozanimahlangu.entities.Book;
import com.thokozanimahlangu.mappers.BookMapper;
import com.thokozanimahlangu.models.BookDTO;
import com.thokozanimahlangu.repositories.BookRepository;
import com.thokozanimahlangu.repositories.BookSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceJPA implements BookService{

	private final BookRepository bookRepository;
	private final BookMapper bookMapper;
	
	public List<BookDTO> listBooks(String title, String author, String ISBN, Year publicationYear) {
		
		Specification<Book> spec = Specification.where(BookSpecification.hasAuthor(author))
												.and(BookSpecification.hasTitle(title))
												.and(BookSpecification.hasPublicationYear(publicationYear))
												.and(BookSpecification.hasISBN(ISBN));
		
		return bookRepository.findAll(spec)
							.stream()
							.map(book -> bookMapper.bookToBookDTO(book))
							.collect(Collectors.toList());
					   
	}
	
	public Optional<BookDTO> getBookById(UUID id) {
		
		return Optional.ofNullable(bookMapper.bookToBookDTO(bookRepository.findById(id).orElse(null)));
	}
	
	public BookDTO saveNewBook(BookDTO newBook) {
		
		return bookMapper.bookToBookDTO(bookRepository.save(bookMapper.bookDTOtoBook(newBook)));
	}
	
	public Optional<BookDTO> updateBookById(UUID id, BookDTO bookDto) {
		
		return bookRepository.findById(id)
				.map(foundBook -> {
					foundBook.setAuthor(bookDto.getAuthor());
					foundBook.setISBN(bookDto.getISBN());
					foundBook.setPublicationYear(bookDto.getPublicationYear());
					foundBook.setTitle(bookDto.getTitle());
					
					return bookMapper.bookToBookDTO(bookRepository.save(foundBook));
				});
	}
	
	public Boolean deleteBookById(UUID id) {
		
		if (bookRepository.existsById(id)) {
			
			bookRepository.deleteById(id);
			
			return true;
		}		
		return false;
		}
	
	public Optional<BookDTO> patchBookById(UUID id, BookDTO bookDto) {
		
		return bookRepository.findById(id)
				.map(foundBook -> {
					if (StringUtils.hasText(bookDto.getAuthor())) {
						foundBook.setAuthor(bookDto.getAuthor());
					}
					if (StringUtils.hasText(bookDto.getISBN())) {
						foundBook.setISBN(bookDto.getISBN());
					}
					if (StringUtils.hasText(bookDto.getTitle())) {
						foundBook.setTitle(bookDto.getTitle());
					}
					if (bookDto.getPublicationYear() != null) {
						foundBook.setPublicationYear(bookDto.getPublicationYear());
					}
					
					return bookMapper.bookToBookDTO(bookRepository.save(foundBook));
				});
	}
}
