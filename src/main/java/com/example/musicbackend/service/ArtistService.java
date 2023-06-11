package com.example.musicbackend.service;

import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.entity.Artist;
import com.example.musicbackend.entity.User;
import com.example.musicbackend.exception.custom.NotFoundItemException;

import java.util.List;

public interface ArtistService {
    List<ArtistDto> findAllArtist();

    ArtistDto findById(Long id) throws NotFoundItemException;

    byte[] getPhotoArtistById(Long id);

    ArtistDto insertArtist(ArtistDto artistDto);

    ArtistDto updateArtist(ArtistDto artistDto);
}
