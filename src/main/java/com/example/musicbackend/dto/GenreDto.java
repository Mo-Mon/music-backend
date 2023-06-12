package com.example.musicbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class GenreDto {

    private Long id;

    private String name;

    private List<Long> listSongId;

}
