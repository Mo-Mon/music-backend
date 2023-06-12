package com.example.musicbackend.service;

import com.example.musicbackend.dto.AlbumDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AlbumService {
    List<AlbumDto> findAllAlbum();

    AlbumDto findById(Long id);

    byte[] getPhotoAlbumById(Long id);

    AlbumDto insertAlbum(AlbumDto AlbumDto, MultipartFile file);

    AlbumDto updateAlbum(AlbumDto AlbumDto, MultipartFile file);
}
