package com.example.tambang.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class RealEstateForm {

    private String sigungu;
    private String roadName;
    private String buildType;
    private int floor;
    private Float area;
    private String dealType;
    private int price;
    private int deposit;
    private int monthlyPay;
    private String description;
}
