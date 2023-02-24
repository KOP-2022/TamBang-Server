package com.example.tambang.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BuildInfo {
    private final String buildType;
    private final Long floor;
    private final Double area;
    private final String dealType;
    private final Long price;
    private final Long deposit;
    private final Long monthlyPay;
}
