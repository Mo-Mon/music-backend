package com.example.musicbackend.exception.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage TodoException(Exception ex, WebRequest request) {
        String requestBody = "";
        if (request instanceof ServletWebRequest) {
            ServletWebRequest servletRequest = (ServletWebRequest) request;
            HttpServletRequest httpServletRequest = servletRequest.getRequest();
            try {
                requestBody = httpServletRequest.getReader().lines().collect(Collectors.joining());
            } catch (IOException e) {
                // Xử lý IOException nếu cần thiết
            }
        }
        String info = String.format("rất tiếc đã có lỗi xảy ra exception (%s) data web requet (%s)", ex.getMessage(),requestBody );
        return new ErrorMessage(info);
    }
}
