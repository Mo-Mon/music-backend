package com.example.musicbackend.service;

import com.example.musicbackend.dto.AlbumDto;
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

    void deleteAlbum(Long id);
}
