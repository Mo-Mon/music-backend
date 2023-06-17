package com.example.musicbackend.exception.advice;

import com.example.musicbackend.Utils.JsonLogicUtil;
import com.example.musicbackend.exception.custom.AuthException;
import com.example.musicbackend.exception.custom.BadRequestException;
import com.example.musicbackend.exception.custom.FileWrongException;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionController{

    @ExceptionHandler({BadRequestException.class, FileWrongException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage todoBadRequestException(Exception ex, WebRequest request) {
        String info = String.format("rất tiếc đã có lỗi xảy ra exception (%s) ", ex.getMessage());
        return new ErrorMessage(info);
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage todoAuthException(Exception ex, WebRequest request) {
        String info = String.format("rất tiếc đã có lỗi xảy ra exception (%s) ", ex.getMessage());
        return new ErrorMessage(info);
    }

    @ExceptionHandler(NotFoundItemException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage todoNotFoundException(Exception ex, WebRequest request) {
        String info = String.format("rất tiếc đã có lỗi xảy ra exception (%s) ", ex.getMessage());
        return new ErrorMessage(info);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage todoException(Exception ex, WebRequest request) {
        String info = String.format("rất tiếc đã có lỗi xảy ra exception (%s) ",ex.getMessage());
        return new ErrorMessage(info);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> body = new HashMap<>();

        for(FieldError err: ex.getBindingResult().getFieldErrors()){
            body.put(err.getField(), err.getDefaultMessage());
        }

        Map<String, Map<String, String>> error = new HashMap<>();
        error.put("error",body);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
