package com.example.musicbackend.service;

import com.example.musicbackend.dto.GenreDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAllGenre();

    GenreDto findById(Long id);

    byte[] getPhotoGenreById(Long id);

    GenreDto insertGenre(GenreDto genreDto, MultipartFile file);

    GenreDto updateGenre(GenreDto genreDto, MultipartFile file);
}
