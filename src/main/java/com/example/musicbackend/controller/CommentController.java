package com.example.musicbackend.controller;

import com.example.musicbackend.dto.CommentDto;
import com.example.musicbackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/getCommentsBySong/{songId}")
    public List<CommentDto> getCommentsBySong(@PathVariable("songId") Long songId){
        return commentService.getAllCommentBySongId(songId);
    }

    @PostMapping("/create")
    public CommentDto createComment(@RequestBody  CommentDto commentDto){
        return commentService.addCommentForSong(commentDto);
    }

    @PutMapping("/update")
    public CommentDto editComment(@RequestBody  CommentDto commentDto){
        return commentService.editCommentForSong(commentDto);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok("");
    }
}
