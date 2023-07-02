package com.example.musicbackend.controller;

import com.example.musicbackend.Utils.JsonLogicUtil;
import com.example.musicbackend.dto.SongDto;
import com.example.musicbackend.payload.request.SearchSongRequest;
import com.example.musicbackend.service.SongService;
import com.example.musicbackend.validate.ValidateSupport;
import jakarta.validation.Valid;
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

    private final ValidateSupport validateSupport;

    @GetMapping("/searchAll")
    public ResponseEntity<?> searchAll(@Valid @RequestBody SearchSongRequest searchSongRequest){
        return ResponseEntity.ok(songService.searchAll(searchSongRequest));
    }

    @GetMapping("/searchByUser")
    public ResponseEntity<?> searchByUser(@Valid @RequestBody SearchSongRequest searchSongRequest){
        return ResponseEntity.ok(songService.searchByUser(searchSongRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(songService.findById(id));
    }

    @GetMapping(value = "/data/{id}", produces = "audio/mpeg")
    public ResponseEntity<?> getDataById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(songService.getDataSongById(id));
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(songService.getPhotoSongById(id));
    }

    @PostMapping(value = "/admin/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestParam("songDto") String strSongDto, @RequestParam("data") MultipartFile data, @RequestParam("photo") MultipartFile photo){
        SongDto songDto = (SongDto) JsonLogicUtil.convertJsonToObject(strSongDto, SongDto.class);
        validateSupport.check(songDto);
        return ResponseEntity.ok(songService.insertSong(songDto, data, photo));
    }

    @PutMapping(value = "/admin/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestParam("songDto") String strSongDto, @RequestParam("data") MultipartFile data, @RequestParam("photo") MultipartFile photo){
        SongDto songDto = (SongDto) JsonLogicUtil.convertJsonToObject(strSongDto, SongDto.class);
        validateSupport.check(songDto);
        return ResponseEntity.ok(songService.updateSong(songDto, data, photo));
    }

    @PutMapping(value = "/admin/updateDto")
    public ResponseEntity<?> updateDto(@Valid @RequestBody SongDto songDto){
        return ResponseEntity.ok(songService.updateSong(songDto));
    }

    @DeleteMapping(value = "/admin/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        songService.deleteSong(id);
        return ResponseEntity.ok("");
    }
}
