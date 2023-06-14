package com.example.musicbackend.service;

import com.example.musicbackend.dto.PlaylistDto;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.payload.request.SearchPlaylistRequest;
import com.example.musicbackend.payload.response.PlaylistResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PlaylistService {
    List<SongDto> getSongsInPlaylist(Long id);

    PlaylistDto addSongToPlaylist(Long id, Long songId);

    PlaylistDto deleteSongToPlaylist(Long id, Long songId);

    Page<PlaylistResponse> search(SearchPlaylistRequest searchPlaylistRequest);

    PlaylistDto findById(Long id);

    PlaylistDto insertPlayList(PlaylistDto playlistDto);

    PlaylistDto updatePlayList(PlaylistDto playlistDto);

    void deletePlaylist(Long id);
}
