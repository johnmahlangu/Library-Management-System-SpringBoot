package com.thokozanimahlangu.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Book Already Returned")
public class BookAlreadyReturnedException extends RuntimeException{

}
