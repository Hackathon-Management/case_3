package ru.hack.Hackathon_Management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping
    public String hello() {
        return "Hello World";
    }
}
