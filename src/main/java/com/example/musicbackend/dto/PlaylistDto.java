package com.example.musicbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlaylistDto {
    private Long id;

    private String name;

    @JsonProperty("userCreateId")
    private Long idCreateBy;

    private List<Long> songs;
}
