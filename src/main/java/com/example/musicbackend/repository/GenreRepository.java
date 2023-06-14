package com.example.musicbackend.repository;

import com.example.musicbackend.entity.Genre;
import com.example.musicbackend.payload.response.GenreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("select new com.example.musicbackend.payload.response.GenreResponse(g.id, g.name) from Genre g where g.deleteFlag = false and (:name is null or g.name like %:name%)")
    Page<GenreResponse> search(String name, Pageable pageable);

    @Query("select g from Genre g  where g.id = :id and g.deleteFlag = false ")
    Optional<Genre> findById(Long id);

    @Query("select g from Genre g  where g.deleteFlag = false ")
    List<Genre> findAll();
}
