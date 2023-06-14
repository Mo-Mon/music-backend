package com.example.musicbackend.service;

import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.entity.Song;
import com.example.musicbackend.payload.request.SearchSongRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface SongService {
    Page<SongDto> searchAll(SearchSongRequest searchSongRequest);

    Page<SongDto> searchByUser(SearchSongRequest searchSongRequest);

    SongDto findById(Long id);

    byte[] getDataSongById(Long id);

    SongDto insertSong(SongDto songDto, MultipartFile data, MultipartFile photo);

    SongDto updateSong(SongDto songDto, MultipartFile data, MultipartFile photo);

    SongDto updateSong(SongDto songDto);

    void deleteSong(Long id);

    byte[] getPhotoSongById(Long id);

    SongDto getSongDto(Song song);

    SongDto clickLikeSong(Long songId);
}
