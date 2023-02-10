package com.example.tambang.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class FacilityForm {
    //위도와 경도
    private String latitude;  // y 위도
    private String longitude; // x 경도
    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String category_name;
    private String distance;
    private String id;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;

}
