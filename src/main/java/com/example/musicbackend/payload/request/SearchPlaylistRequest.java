package com.example.musicbackend.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPlaylistRequest {

    private String name;

    private Integer pageCurrent;

    private Integer size;

}
