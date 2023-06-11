package com.example.musicbackend.exception.custom;

public class NotFoundItemException extends RuntimeException{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 1L;

    public NotFoundItemException(String message){
        super(message);
    }
}
