package com.example.musicbackend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchAlbumRequest {

    private String name;

    private Integer pageCurrent;

    private Integer size;

}
