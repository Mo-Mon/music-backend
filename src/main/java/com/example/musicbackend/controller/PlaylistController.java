package com.example.musicbackend.controller;

import com.example.musicbackend.dto.PlaylistDto;
import com.example.musicbackend.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/playlist")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

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
}
