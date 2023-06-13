package com.example.musicbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    private Long id;

    private String content;

    private Boolean isEdited;

    private Date updateAt;

    private Long songId;

    @JsonProperty("userCreateId")
    private Long idCreateBy;

}
