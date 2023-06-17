package com.example.musicbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class PlaylistDto {
    private Long id;

    @NotNull(message = "không được phép null")
    @NotEmpty(message = "không được phép empty")
    @Length(max = 255, message = "độ dài tôi đa là 255 kí tự")
    private String name;

    @JsonProperty("userCreateId")
    private Long idCreateBy;

    private List<Long> songs;
}
