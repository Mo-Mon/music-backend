package com.example.musicbackend.service;

import com.example.musicbackend.dto.SongDto;
import org.springframework.web.multipart.MultipartFile;

public interface SongService {
    SongDto findById(Long id);

    byte[] getDataSongById(Long id);

    SongDto insertSong(SongDto songDto, MultipartFile file);

    SongDto updateSong(SongDto songDto, MultipartFile file);

    void deleteSong(Long id);
}
