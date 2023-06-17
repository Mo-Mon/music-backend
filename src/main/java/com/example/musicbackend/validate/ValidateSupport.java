package com.example.musicbackend.validate;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class ValidateSupport {

    public static boolean isImageFile(MultipartFile file) {
        //Let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"png","jpg","jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    public static boolean isMusicFile(MultipartFile file) {
        //Let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[] {"mp3","wav","flac"})
                .contains(fileExtension.trim().toLowerCase());
    }

    public static boolean checkLength(MultipartFile file){
        //file phải <= 2Mb do blob chỉ lưu được 2mb trong db
        float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
        return fileSizeInMegabytes > 2.0f ;
    }

}
