package com.example.musicbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDto {

    private Long id;

    private String name;

    private Long artistId;

    private Long albumId;

    private List<Long> listGenreId;

}
