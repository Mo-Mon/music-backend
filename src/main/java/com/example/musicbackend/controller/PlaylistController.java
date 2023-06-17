package com.example.musicbackend.controller;

import com.example.musicbackend.dto.PlaylistDto;
import com.example.musicbackend.payload.request.SearchPlaylistRequest;
import com.example.musicbackend.payload.request.SearchSongRequest;
import com.example.musicbackend.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping("/addSongToPlaylist/{id}-{songId}")
    public ResponseEntity<?> addSongToPlaylist(@PathVariable Long id, @PathVariable Long songId){
        return ResponseEntity.ok(playlistService.addSongToPlaylist(id, songId));
    }

    @DeleteMapping("/deleteSongToPlaylist/{id}-{songId}")
    public ResponseEntity<?> deleteSongToPlaylist(@PathVariable Long id, @PathVariable Long songId){
        return ResponseEntity.ok(playlistService.deleteSongToPlaylist(id, songId));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchPlaylistRequest searchPlaylistRequest){
        return ResponseEntity.ok(playlistService.search(searchPlaylistRequest));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(playlistService.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PlaylistDto playlistDto){
        return ResponseEntity.ok(playlistService.insertPlayList(playlistDto));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody PlaylistDto playlistDto){
        return ResponseEntity.ok(playlistService.updatePlayList(playlistDto));
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        playlistService.deletePlaylist(id);
        return ResponseEntity.ok("");
    }

    @GetMapping("/getSongs/{id}")
    public ResponseEntity<?> getSongs(@PathVariable Long id, @RequestBody SearchSongRequest searchSongRequest){
        return ResponseEntity.ok(playlistService.getSongsInPlaylist(id, searchSongRequest));
    }

}
