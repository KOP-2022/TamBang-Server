package com.example.tambang.repository;

import com.example.tambang.domain.RealEstate;

public interface RealEstateRepository {
    void save(RealEstate realEstate, String memberEmail);
    RealEstate findOne(Long id);
}
