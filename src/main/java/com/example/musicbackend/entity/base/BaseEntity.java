package com.example.musicbackend.entity.base;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Date updateAt;
    private Boolean deleteFlag;
    private String updateBy;
    private Date createAt;
    private String createBy;
}
