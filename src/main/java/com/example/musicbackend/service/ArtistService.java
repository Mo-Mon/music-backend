package com.example.musicbackend.service;

import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.payload.request.SearchArtistRepuest;
import com.example.musicbackend.payload.request.SearchSongRequest;
import com.example.musicbackend.payload.response.ArtistResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArtistService {

    Page<SongDto> getSongsInArtist(Long id, SearchSongRequest searchSongRequest);

    ArtistDto addSongToArtist(Long id, Long songId);

    ArtistDto deleteSongToArtist(Long id, Long songId);

    Page<ArtistResponse> search(SearchArtistRepuest searchArtistRepuest);

    List<ArtistDto> findAllArtist();

    ArtistDto findById(Long id) throws NotFoundItemException;

    byte[] getPhotoArtistById(Long id);

    ArtistDto insertArtist(ArtistDto artistDto, MultipartFile file);

    ArtistDto updateArtist(ArtistDto artistDto, MultipartFile file);

    void deleteArtist(Long id);

    ArtistDto updateArtist(ArtistDto artistDto);
}
