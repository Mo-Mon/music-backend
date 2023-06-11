package com.example.musicbackend.controller;

import com.example.musicbackend.dto.AlbumDto;
import com.example.musicbackend.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/album")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(albumService.findAllAlbum());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(albumService.findById(id));
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(albumService.getPhotoAlbumById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AlbumDto albumDto){
        return ResponseEntity.ok(albumService.insertAlbum(albumDto));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody AlbumDto albumDto){
        return ResponseEntity.ok(albumService.updateAlbum(albumDto));
    }

}
