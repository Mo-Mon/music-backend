package com.example.musicbackend.repository;

import com.example.musicbackend.dto.AlbumDto;
import com.example.musicbackend.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("select new com.example.musicbackend.dto.AlbumDto(a.id, a.name, a.artist.id, a.artist.name) from Album a where :name is NULL OR a.name like %:name%")
    Page<AlbumDto> search(String name, Pageable pageable);

}
