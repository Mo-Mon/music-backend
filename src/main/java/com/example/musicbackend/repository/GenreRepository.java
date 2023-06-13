package com.example.musicbackend.repository;

import com.example.musicbackend.entity.Genre;
import com.example.musicbackend.payload.response.GenreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("select new com.example.musicbackend.payload.response.GenreResponse() from Genre g where g.deleteFlag = false and (:name is null or g.name like %:name%)")
    Page<GenreResponse> search(String name, Pageable pageable);
}
