package com.example.musicbackend.controller;

import com.example.musicbackend.Utils.JsonLogicUtil;
import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.service.ArtistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(artistService.getPhotoArtistById(id));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestParam("artistDto") String strArtistDto, @RequestParam("file") MultipartFile file){
        ArtistDto artistDto = (ArtistDto) JsonLogicUtil.convertJsonToObject(strArtistDto, ArtistDto.class);
        return ResponseEntity.ok(artistService.insertArtist(artistDto, file));
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestParam("artistDto")String strArtistDto, @RequestParam("file") MultipartFile file){
        ArtistDto artistDto = (ArtistDto) JsonLogicUtil.convertJsonToObject(strArtistDto, ArtistDto.class);
        return ResponseEntity.ok(artistService.updateArtist(artistDto, file));
    }



}
