package com.example.musicbackend.repository;

import com.example.musicbackend.entity.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.song.id = :id AND c.deleteFlag = false")
    Page<Comment> findCommentByIdSong(Long id, Pageable pageable);

    @Query("select c from Comment c  where c.id = :id and c.deleteFlag = false ")
    Optional<Comment> findById(Long id);

    @Query("select c from Comment c  where c.deleteFlag = false ")
    List<Comment> findAll();
}
