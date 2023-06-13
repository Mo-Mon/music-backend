package com.example.musicbackend.repository;

import com.example.musicbackend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.song.id = :id")
    List<Comment> findCommentByIdSong(Long id);
}
