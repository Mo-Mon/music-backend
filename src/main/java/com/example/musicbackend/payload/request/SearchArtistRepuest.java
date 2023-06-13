package com.example.musicbackend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchArtistRepuest {

    private String name;

    private String info;

    private Integer pageCurrent;

    private Integer size;

}
