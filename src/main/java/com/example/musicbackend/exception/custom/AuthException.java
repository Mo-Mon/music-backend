package com.example.musicbackend.exception.custom;

public class AuthException extends RuntimeException{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 1L;

    public AuthException(String message){
        super(message);
    }
}
