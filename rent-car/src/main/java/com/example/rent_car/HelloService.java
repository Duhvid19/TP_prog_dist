package com.example.rent_car;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloService {
    @GetMapping("/")
    public String hello() {
    return "hello";
    }
}