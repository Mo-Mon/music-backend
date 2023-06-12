package com.example.musicbackend.service;

import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArtistService {
    List<ArtistDto> findAllArtist();

    ArtistDto findById(Long id) throws NotFoundItemException;

    byte[] getPhotoArtistById(Long id);

    ArtistDto insertArtist(ArtistDto artistDto, MultipartFile file);

    ArtistDto updateArtist(ArtistDto artistDto, MultipartFile file);
}
