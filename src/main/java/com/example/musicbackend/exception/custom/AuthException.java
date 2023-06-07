package com.example.musicbackend.exception.custom;

public class AuthException extends Exception{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 1L;

    public AuthException(String message){
        super(message);
    }
}
