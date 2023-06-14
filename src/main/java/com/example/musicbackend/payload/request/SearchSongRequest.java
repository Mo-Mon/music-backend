package com.example.musicbackend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchSongRequest {

    private String name;

    private String nameArtist;

    private String nameAlbum;

    private List<Long> listIdGenre;

    private Integer pageCurrent;

    private Integer size;
}
