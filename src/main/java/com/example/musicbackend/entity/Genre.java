package com.example.musicbackend.entity;

import com.example.musicbackend.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Genre extends BaseEntity {

    private String name;

    @Lob
    @Column(length=16777215)
    private byte[] photoData;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER)
    private List<Song> songs = new ArrayList<>();

}