package com.example.tambang.repository;

import com.example.tambang.domain.Facility;
import com.example.tambang.domain.RealEstate;

import java.util.List;

public interface RealEstateRepository {
    void save(RealEstate realEstate, String memberEmail);
    RealEstate findOne(Long id);

    List<Facility> findAroundFacilities(RealEstate realEstate);
}
