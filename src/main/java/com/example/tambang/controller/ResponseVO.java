package com.example.tambang.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class ResponseVO {

    private Map<String, Object> data = new HashMap<>();
    private boolean success;
}
