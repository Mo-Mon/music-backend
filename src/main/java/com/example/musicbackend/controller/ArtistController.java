package com.example.musicbackend.controller;

import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/artist")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(artistService.findAllArtist());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(artistService.findById(id));
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(artistService.getPhotoArtistById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ArtistDto artistDto){
        return ResponseEntity.ok(artistService.insertArtist(artistDto));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ArtistDto artistDto){
        return ResponseEntity.ok(artistService.updateArtist(artistDto));
    }



}
