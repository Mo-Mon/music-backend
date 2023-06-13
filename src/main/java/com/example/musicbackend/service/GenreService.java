package com.example.musicbackend.service;

import com.example.musicbackend.dto.GenreDto;
import com.example.musicbackend.payload.request.SearchGenreRequest;
import com.example.musicbackend.payload.response.GenreResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GenreService {
    Page<GenreResponse> search(SearchGenreRequest searchGenreRequest);

    List<GenreDto> findAllGenre();

    GenreDto findById(Long id);

    byte[] getPhotoGenreById(Long id);

    GenreDto insertGenre(GenreDto genreDto, MultipartFile file);

    GenreDto updateGenre(GenreDto genreDto, MultipartFile file);

    void deleteGenre(Long id);
}
