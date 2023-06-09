package com.example.musicbackend.repository;

import com.example.musicbackend.entity.Playlist;
import com.example.musicbackend.entity.Song;
import com.example.musicbackend.payload.response.PlaylistResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    @Query("select new com.example.musicbackend.payload.response.PlaylistResponse(p.id, p.name) from Playlist p where p.deleteFlag = false and p.idCreateBy = :userId and (:name is null or p.name like %:name%)")
    Page<PlaylistResponse> search(String name, Long userId, Pageable pageable);

    @Query("select p from Playlist p  where p.id = :id and p.deleteFlag = false ")
    Optional<Playlist> findById(Long id);

    @Query("select p from Playlist p  where p.deleteFlag = false ")
    List<Playlist> findAll();

    @Query("SELECT s FROM Playlist p inner join p.songs s on (p.id = :idPlayList) LEFT JOIN s.genres g ON (g.deleteFlag = false ) WHERE s.deleteFlag = false and s.artist.name like %:nameArtist% and s.album.name like %:nameAlbum%  and s.name LIKE %:name% AND g.id IN (:listIdGenre)")
    Page<Song> searchSongByPlaylist(String name, String nameArtist, String nameAlbum, Long idPlayList, List<Long> listIdGenre, Pageable pageable);


}
