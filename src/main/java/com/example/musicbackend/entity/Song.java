package com.example.musicbackend.entity;

import com.example.musicbackend.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class Song extends BaseEntity {

    private String name;

    @Lob
    @Column(length=16777215)
    private byte[] data;

    @Lob
    @Column(length=16777215)
    private byte[] photo;

    @ManyToOne(fetch = FetchType.EAGER)
    private Artist artist;

    @ManyToOne(fetch = FetchType.EAGER)
    private Album album;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "SONG_GENRE",
            joinColumns = @JoinColumn(name = "SONG_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID"))
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany(mappedBy = "likedSongs", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<User> likedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "song", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

}
