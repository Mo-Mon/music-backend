package com.example.musicbackend.entity;

import com.example.musicbackend.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class Album extends BaseEntity {

    private String name;

    @Lob
    @Column(length=16777215)
    private byte[] albumArtData;

    @ManyToOne(fetch = FetchType.EAGER)
    private Artist artist;

    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Song> songs = new ArrayList<>();

}