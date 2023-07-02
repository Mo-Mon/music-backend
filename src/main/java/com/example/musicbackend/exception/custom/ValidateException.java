package com.example.musicbackend.exception.custom;

import lombok.Data;

import java.util.Map;

@Data
public class ValidateException extends RuntimeException{
    /**
     * Serial number
     */
    private static final long serialVersionUID = 1L;

    private Map<String, Map<String, String>> error;

    public ValidateException( Map<String, Map<String, String>> error){
        this.error = error;
    }
}
