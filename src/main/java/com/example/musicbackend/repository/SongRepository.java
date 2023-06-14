package com.example.musicbackend.repository;

import com.example.musicbackend.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s LEFT JOIN s.genres g ON (g.deleteFlag = false )  WHERE s.deleteFlag = false and s.artist.name like %:nameArtist% and s.album.name like %:nameAlbum%  and s.name LIKE %:name% AND g.id IN (:listIdGenre)")
    Page<Song> searchAll(String name, String nameArtist, String nameAlbum, List<Long> listIdGenre, Pageable pageable);

    @Query("SELECT s FROM Song s LEFT JOIN s.genres g ON (g.deleteFlag = false )  WHERE s.deleteFlag = false and s.artist.name like %:nameArtist% and s.album.name like %:nameAlbum%  and s.name LIKE %:name% AND g.id IN (:listIdGenre) and s.idCreateBy = :userId")
    Page<Song> searchByUser(String name, String nameArtist, String nameAlbum, Long userId, List<Long> listIdGenre, Pageable pageable);

    @Query("SELECT count(u) FROM Song s inner join s.likedUsers u on ( u.deleteFlag = false ) where u.deleteFlag = false and s.id = :id")
    Integer countLike(Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Song s inner join s.likedUsers u on ( u.deleteFlag = false ) where u.deleteFlag = false and s.id = :id and u.id = :userId")
    Boolean checkLiked(Long id, Long userId);

    @Query("select s from Song s  where s.id = :id and s.deleteFlag = false ")
    Optional<Song> findById(Long id);

    @Query("select s from Song s  where s.deleteFlag = false ")
    List<Song> findAll();

}
