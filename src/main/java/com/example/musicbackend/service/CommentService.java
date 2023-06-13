package com.example.musicbackend.service;

import com.example.musicbackend.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getAllCommentBySongId(Long songId);

    CommentDto addCommentForSong(CommentDto commentDto);

    CommentDto editCommentForSong(CommentDto commentDto);

    void deleteComment(Long id);
}
