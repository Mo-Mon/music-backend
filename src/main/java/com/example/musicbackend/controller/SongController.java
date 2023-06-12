package com.example.musicbackend.controller;

import com.example.musicbackend.Utils.JsonLogicUtil;
import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/song")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(songService.findById(id));
    }

    @GetMapping(value = "/data/{id}", produces = "audio/mpeg")
    public ResponseEntity<?> getDataById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(songService.getDataSongById(id));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestParam("songDto") String strSongDto, @RequestParam("file") MultipartFile file){
        SongDto songDto = (SongDto) JsonLogicUtil.convertJsonToObject(strSongDto, SongDto.class);
        return ResponseEntity.ok(songService.insertSong(songDto, file));
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestParam("songDto") String strSongDto, @RequestParam("file") MultipartFile file){
        SongDto songDto = (SongDto) JsonLogicUtil.convertJsonToObject(strSongDto, SongDto.class);
        return ResponseEntity.ok(songService.updateSong(songDto, file));
    }

}
