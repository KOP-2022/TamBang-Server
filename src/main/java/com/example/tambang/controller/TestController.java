package com.example.tambang.controller;

import com.example.tambang.service.FirstService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Controller + ResponseBody: Json 형태로 객체 데이터를 반환
@RestController
@RequiredArgsConstructor
public class TestController {

    private final FirstService firstService;

    @GetMapping("/tambang")
    public Map<String, Object> firstController(){
        return firstService.getFirstData();
    }
}
