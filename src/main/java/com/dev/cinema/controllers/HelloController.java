package com.dev.cinema.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/Hello")
    public String sayHello() {
        return "/Hello";
    }
}
