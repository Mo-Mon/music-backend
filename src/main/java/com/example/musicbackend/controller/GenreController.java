package com.example.musicbackend.controller;

import com.example.musicbackend.Utils.JsonLogicUtil;
import com.example.musicbackend.dto.GenreDto;
import com.example.musicbackend.payload.request.SearchGenreRequest;
import com.example.musicbackend.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchGenreRequest searchGenreRequest){
        return ResponseEntity.ok(genreService.search(searchGenreRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(genreService.findAllGenre());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(genreService.findById(id));
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(genreService.getPhotoGenreById(id));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestParam("genreDto") String strGenreDto, @RequestParam("file") MultipartFile file){
        GenreDto genreDto = (GenreDto) JsonLogicUtil.convertJsonToObject(strGenreDto, GenreDto.class);
        return ResponseEntity.ok(genreService.insertGenre(genreDto, file));
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestParam("genreDto") String strGenreDto, @RequestParam("file") MultipartFile file){
        GenreDto genreDto = (GenreDto) JsonLogicUtil.convertJsonToObject(strGenreDto, GenreDto.class);
        return ResponseEntity.ok(genreService.updateGenre(genreDto, file));
    }

    @PutMapping(value = "/updateDto")
    public ResponseEntity<?> update(@RequestBody GenreDto genreDto){
        return ResponseEntity.ok(genreService.updateGenre(genreDto));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        genreService.deleteGenre(id);
        return ResponseEntity.ok("");
    }

}
