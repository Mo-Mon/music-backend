package com.example.musicbackend.dto;

import com.example.musicbackend.entity.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDto {

    private Long id;

    private String name;

    private String info;

    private List<Long> listAlbumId;
}
