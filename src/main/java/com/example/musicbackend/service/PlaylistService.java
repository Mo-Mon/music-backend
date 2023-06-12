package com.example.musicbackend.service;

import com.example.musicbackend.dto.PlaylistDto;

public interface PlaylistService {
    PlaylistDto findById(Long id);

    PlaylistDto insertPlayList(PlaylistDto playlistDto);

    PlaylistDto updatePlayList(PlaylistDto playlistDto);
}
