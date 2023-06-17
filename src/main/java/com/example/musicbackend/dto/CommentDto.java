package com.example.musicbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
public class CommentDto {

    private Long id;

    @NotNull(message = "không được phép null")
    @NotEmpty(message = "không được phép empty")
    @Length(max = 255, message = "độ dài tôi đa là 255 kí tự")
    private String content;

    private Boolean isEdited;

    private Date updateAt;

    private Long songId;

    @JsonProperty("userCreateId")
    private Long idCreateBy;

}
