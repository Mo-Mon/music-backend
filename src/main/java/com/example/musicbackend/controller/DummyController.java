package com.example.musicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dummy")
@RequiredArgsConstructor
public class DummyController {

    @GetMapping("/admin1/show")
    public String show1() {
        return "hello welcome to my website";
    }

    @GetMapping ("/admin/show")
    public ResponseEntity<?> show(
    ) {
        return ResponseEntity.ok("test link");
    }
}
