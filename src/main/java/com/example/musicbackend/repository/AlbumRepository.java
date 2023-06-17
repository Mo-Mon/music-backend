package com.example.musicbackend.repository;

import com.example.musicbackend.dto.AlbumDto;
import com.example.musicbackend.entity.Album;
import com.example.musicbackend.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("select new com.example.musicbackend.dto.AlbumDto(a.id, a.name, a.artist.id, a.artist.name) from Album a where a.deleteFlag = false AND (:name is NULL OR a.name like %:name%)")
    Page<AlbumDto> search(String name, Pageable pageable);

    @Query("select a from Album a  where a.id = :id and a.deleteFlag = false ")
    Optional<Album> findById(Long id);

    @Query("select a from Album a  where a.deleteFlag = false ")
    List<Album> findAll();

    @Query("SELECT s FROM Song s LEFT JOIN s.genres g ON (g.deleteFlag = false )  WHERE s.deleteFlag = false and s.artist.name like %:nameArtist% and s.album.id = :idAlbum  and s.name LIKE %:name% AND g.id IN (:listIdGenre)")
    Page<Song> searchSongByAlbum(String name, String nameArtist, Long idAlbum, List<Long> listIdGenre, Pageable pageable);


}
