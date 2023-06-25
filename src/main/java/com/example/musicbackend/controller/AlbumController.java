package com.example.musicbackend.controller;

import com.example.musicbackend.Utils.JsonLogicUtil;
import com.example.musicbackend.dto.AlbumDto;
import com.example.musicbackend.payload.request.SearchAlbumRequest;
import com.example.musicbackend.payload.request.SearchSongRequest;
import com.example.musicbackend.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/album")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(albumService.findAllAlbum());
    }

    @GetMapping("/getSongs/{id}")
    public ResponseEntity<?> getSongs(@PathVariable Long id, @Valid @RequestBody SearchSongRequest searchSongRequest){
        return ResponseEntity.ok(albumService.getSongsInAlbum(id, searchSongRequest));
    }

    @PostMapping("/admin/addSongToAlbum/{id}-{songId}")
    public ResponseEntity<?> addSongToAlbum(@PathVariable Long id, @PathVariable Long songId){
        return ResponseEntity.ok(albumService.addSongToAlbum(id, songId));
    }

    @DeleteMapping("/admin/deleteSongToAlbum/{id}-{songId}")
    public ResponseEntity<?> deleteSongToAlbum(@PathVariable Long id, @PathVariable Long songId){
        return ResponseEntity.ok(albumService.deleteSongToAlbum(id, songId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(albumService.findById(id));
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(albumService.getPhotoAlbumById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody SearchAlbumRequest searchAlbumRequest){
        return ResponseEntity.ok().body(albumService.search(searchAlbumRequest.getName(),
                PageRequest.of(searchAlbumRequest.getPageCurrent(), searchAlbumRequest.getSize())));
    }

    @PostMapping(value = "/admin/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestParam("albumDto") String strAlbumDto, @RequestParam("file") MultipartFile file){
        @Valid AlbumDto albumDto = (AlbumDto) JsonLogicUtil.convertJsonToObject(strAlbumDto, AlbumDto.class);
        return ResponseEntity.ok(albumService.insertAlbum(albumDto, file));
    }

    @PutMapping(value = "/admin/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestParam("albumDto") String strAlbumDto, @RequestParam("file")  MultipartFile file){
        @Valid AlbumDto albumDto = (AlbumDto) JsonLogicUtil.convertJsonToObject(strAlbumDto, AlbumDto.class);
        return ResponseEntity.ok(albumService.updateAlbum(albumDto,file));
    }

    @PutMapping(value = "/admin/updateDto")
    public ResponseEntity<?> updateDto(@Valid @RequestBody AlbumDto albumDto){
        return ResponseEntity.ok(albumService.updateAlbum(albumDto));
    }

    @DeleteMapping(value = "/admin/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        albumService.deleteAlbum(id);
        return ResponseEntity.ok("");
    }
}
