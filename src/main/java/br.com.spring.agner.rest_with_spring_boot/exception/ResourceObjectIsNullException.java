package br.com.spring.agner.rest_with_spring_boot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceObjectIsNullException extends RuntimeException {
    public ResourceObjectIsNullException() {
        super("Is is not allowed to persist a null object");
    }
    public ResourceObjectIsNullException(String message) {
        super(message);
    }
}
