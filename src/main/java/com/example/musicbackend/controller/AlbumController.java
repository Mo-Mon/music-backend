package com.example.musicbackend.controller;

import com.example.musicbackend.Utils.JsonLogicUtil;
import com.example.musicbackend.dto.AlbumDto;
import com.example.musicbackend.payload.request.SearchAlbumRequest;
import com.example.musicbackend.service.AlbumService;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(albumService.findById(id));
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getPhotoById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(albumService.getPhotoAlbumById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchAlbumRequest searchAlbumRequest){
        return ResponseEntity.ok().body(albumService.search(searchAlbumRequest.getName(),
                PageRequest.of(searchAlbumRequest.getPageCurrent(), searchAlbumRequest.getSize())));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestParam("albumDto") String strAlbumDto, @RequestParam("file") MultipartFile file){
        AlbumDto albumDto = (AlbumDto) JsonLogicUtil.convertJsonToObject(strAlbumDto, AlbumDto.class);
        return ResponseEntity.ok(albumService.insertAlbum(albumDto, file));
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestParam("albumDto") String strAlbumDto, @RequestParam("file")  MultipartFile file){
        AlbumDto albumDto = (AlbumDto) JsonLogicUtil.convertJsonToObject(strAlbumDto, AlbumDto.class);
        return ResponseEntity.ok(albumService.updateAlbum(albumDto,file));
    }

    @PutMapping(value = "/updateDto")
    public ResponseEntity<?> updateDto(@RequestBody AlbumDto albumDto){
        return ResponseEntity.ok(albumService.updateAlbum(albumDto));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        albumService.deleteAlbum(id);
        return ResponseEntity.ok("");
    }
}
