package com.example.musicbackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDto {

    private Long id;

    @NotNull(message = "không được phép null")
    @NotEmpty(message = "không được phép empty")
    @Length(max = 255, message = "độ dài tôi đa là 255 kí tự")
    private String name;

    @Length(max = 255, message = "độ dài tôi đa là 255 kí tự")
    private String info;

    private List<Long> listAlbumId;
}
