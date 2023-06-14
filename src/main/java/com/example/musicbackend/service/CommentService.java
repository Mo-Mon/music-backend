package com.example.musicbackend.service;

import com.example.musicbackend.dto.CommentDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

    Page<CommentDto> getAllCommentBySongId(Long songId, Integer index);

    CommentDto addCommentForSong(CommentDto commentDto);

    CommentDto editCommentForSong(CommentDto commentDto);

    void deleteComment(Long id);
}
