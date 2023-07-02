package com.example.musicbackend.validate;

import com.example.musicbackend.exception.custom.BadRequestException;
import com.example.musicbackend.exception.custom.ValidateException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.map.AbstractOrderedMapDecorator;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ValidateSupport {

    private final Validator validator;

    public void check(Object obj){
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        if (!violations.isEmpty()) {
            Map<String, Map<String, String>> error = new HashMap<>();
            Map<String, String> body = new HashMap<>();
            for (ConstraintViolation<Object> violation : violations) {
                body.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            error.put("error", body);
            throw new ValidateException(error);
        }
    }

    public boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png","jpg","jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    public boolean isMusicFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"mp3","wav","flac"})
                .contains(fileExtension.trim().toLowerCase());
    }

    public boolean checkLength(MultipartFile file){
        float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
        return fileSizeInMegabytes <= 2.0f ;
    }

}
