package com.example.musicbackend.repository;

import com.example.musicbackend.entity.Artist;
import com.example.musicbackend.payload.response.ArtistResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Query("select new com.example.musicbackend.payload.response.ArtistResponse(a.id, a.name, a.info) from Artist a where  a.deleteFlag = false AND (:name is null or a.name like %:name%) AND (:info is null or a.info like %:info%)")
    Page<ArtistResponse> search(String name, String info, Pageable pageable);

}
