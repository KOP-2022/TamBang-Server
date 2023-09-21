package com.example.tambang.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RealEstateResult {
    private final Long id;
    private final  Double latitude;
    private final Double longitude;
}
