package com.example.musicbackend.controller;

import com.example.musicbackend.dto.CommentDto;
import com.example.musicbackend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/getCommentsBySong/{songId}")
    public Page<CommentDto> getCommentsBySong(@PathVariable("songId") Long songId, @RequestParam("index") Integer index){
        return commentService.getAllCommentBySongId(songId, index);
    }

    @PostMapping("/create")
    public CommentDto createComment(@Valid  @RequestBody  CommentDto commentDto){
        return commentService.addCommentForSong(commentDto);
    }

    @PutMapping("/update")
    public CommentDto editComment(@Valid @RequestBody  CommentDto commentDto){
        return commentService.editCommentForSong(commentDto);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok("");
    }
}
