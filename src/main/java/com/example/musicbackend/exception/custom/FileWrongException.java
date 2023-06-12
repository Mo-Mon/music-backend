package com.example.musicbackend.exception.custom;

public class FileWrongException extends RuntimeException{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 1L;

    public FileWrongException(String message){
        super(message);
    }
}
