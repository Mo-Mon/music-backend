package com.example.musicbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("")
    public String show(
    ) {
        System.out.println("hello");
        return "hello test 1";
    }
}
