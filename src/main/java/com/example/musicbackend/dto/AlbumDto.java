package com.example.musicbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDto {

    private Long id;

    private String name;

    private Long artistId;

    private String artistName;
}
