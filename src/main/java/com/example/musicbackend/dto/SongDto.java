package com.example.musicbackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDto {

    private Long id;

    @NotNull(message = "không được phép null")
    @NotEmpty(message = "không được phép empty")
    @Length(max = 255, message = "độ dài tôi đa là 255 kí tự")
    private String name;

    private Long artistId;

    private Long albumId;

    private List<Long> listGenreId;

    private Date createAt;

    private Integer countLike;

}
