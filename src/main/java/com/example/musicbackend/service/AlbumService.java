package com.example.musicbackend.service;

import com.example.musicbackend.dto.AlbumDto;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.payload.request.SearchSongRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlbumService {
    Page<AlbumDto> search(String name, Pageable pageable);

    List<AlbumDto> findAllAlbum();

    AlbumDto findById(Long id);

    byte[] getPhotoAlbumById(Long id);

    AlbumDto insertAlbum(AlbumDto AlbumDto, MultipartFile file);

    AlbumDto updateAlbum(AlbumDto AlbumDto, MultipartFile file);

    AlbumDto updateAlbum(AlbumDto albumDto);

    void deleteAlbum(Long id);

    Page<SongDto> getSongsInAlbum(Long id, SearchSongRequest searchSongRequest);

    AlbumDto addSongToAlbum(Long id, Long songId);

    AlbumDto deleteSongToAlbum(Long id, Long songId);
}
