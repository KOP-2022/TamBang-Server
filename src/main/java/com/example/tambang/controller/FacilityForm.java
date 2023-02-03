package com.example.tambang.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class FacilityForm {
    //위도와 경도
    private String latitude;
    private String longitude;
}
