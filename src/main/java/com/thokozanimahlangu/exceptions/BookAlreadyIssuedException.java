package com.thokozanimahlangu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to issue a book
 * that is currently already issued and not yet returned.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Book Already Issued")
public class BookAlreadyIssuedException extends RuntimeException{

}
