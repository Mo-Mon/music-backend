package com.example.musicbackend.exception.custom;

public class BadRequestException extends RuntimeException{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 1L;

    public BadRequestException(String message){
        super(message);
    }
}
