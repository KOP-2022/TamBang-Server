package com.example.tambang.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RealEstateResults {
    private final List<RealEstateResult> realEstateResult;
    private final int size;
}
