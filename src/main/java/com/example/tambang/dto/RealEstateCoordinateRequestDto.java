package com.example.tambang.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RealEstateCoordinateRequestDto {
    private final Double latitude;
    private final Double longitude;
    private final Double radius;
}
