package com.example.musicbackend.entity;

import com.example.musicbackend.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Genre extends BaseEntity {

    private String name;

    @Lob
    private byte[] photo_data;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private List<Song> songs = new ArrayList<>();

}