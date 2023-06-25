package com.example.musicbackend.controller;

import com.example.musicbackend.Utils.JsonLogicUtil;
import com.example.musicbackend.dto.ArtistDto;
import com.example.musicbackend.payload.request.SearchArtistRepuest;
import com.example.musicbackend.payload.request.SearchSongRequest;
import com.example.musicbackend.service.ArtistService;
import jakarta.validation.Valid;
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

    @GetMapping("/search")
    public ResponseEntity<?> search(@Valid  @RequestBody SearchArtistRepuest searchArtistRepuest){
        return ResponseEntity.ok(artistService.search(searchArtistRepuest));
    }

    @PostMapping("/admin/addSongToArtist/{id}-{songId}")
    public ResponseEntity<?> addSongToArtist(@PathVariable Long id, @PathVariable Long songId){
        return ResponseEntity.ok(artistService.addSongToArtist(id, songId));
    }

    @DeleteMapping("/admin/deleteSongToArtist/{id}-{songId}")
    public ResponseEntity<?> deleteSongToArtist(@PathVariable Long id, @PathVariable Long songId){
        return ResponseEntity.ok(artistService.deleteSongToArtist(id, songId));
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(artistService.getPhotoArtistById(id));
    }

    @PostMapping(value = "/admin/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestParam("artistDto") String strArtistDto, @RequestParam("file") MultipartFile file){
        @Valid ArtistDto artistDto = (ArtistDto) JsonLogicUtil.convertJsonToObject(strArtistDto, ArtistDto.class);
        return ResponseEntity.ok(artistService.insertArtist(artistDto, file));
    }

    @PutMapping(value = "/admin/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestParam("artistDto")String strArtistDto, @RequestParam("file") MultipartFile file){
        @Valid ArtistDto artistDto = (ArtistDto) JsonLogicUtil.convertJsonToObject(strArtistDto, ArtistDto.class);
        return ResponseEntity.ok(artistService.updateArtist(artistDto, file));
    }

    @PutMapping(value = "/admin/updateDto")
    public ResponseEntity<?> updateDto(@Valid @RequestBody ArtistDto artistDto){
        return ResponseEntity.ok(artistService.updateArtist(artistDto));
    }

    @DeleteMapping(value = "/admin/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        artistService.deleteArtist(id);
        return ResponseEntity.ok("");
    }

    @GetMapping("/getSongs/{id}")
    public ResponseEntity<?> getSongs(@PathVariable Long id,@Valid @RequestBody SearchSongRequest searchSongRequest){
        return ResponseEntity.ok(artistService.getSongsInArtist(id, searchSongRequest));
    }

}
