package br.com.spring.agner.rest_with_spring_boot.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {
}
