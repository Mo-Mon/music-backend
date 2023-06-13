package com.example.musicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dummy")
@RequiredArgsConstructor
public class DummyController {

    @GetMapping("")
    public String show() {
        return "hello welcome to my website";
    }
}
