package com.thokozanimahlangu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to return a book
 * that has already been marked as returned.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Book Already Returned")
public class BookAlreadyReturnedException extends RuntimeException{

}
