package com.example.tambang.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RealEstateController {

    @PostMapping("/real-estate")
    public void enroll(@RequestBody RealEstateForm form){
        //form은 DTO와 동일하다고 볼 수 있다.
    }
}
