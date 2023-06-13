package com.example.musicbackend.entity;

import com.example.musicbackend.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Comment extends BaseEntity {

    private String content;

    private Boolean isEdited;

    @ManyToOne(fetch = FetchType.EAGER)
    private Song song;

}
