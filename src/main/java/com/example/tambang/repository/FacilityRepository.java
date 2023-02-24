package com.example.tambang.repository;

import com.example.tambang.domain.Facility;
import com.example.tambang.domain.RealEstateFacility;

public interface FacilityRepository {
    void enrollWithRealEstate(Facility facility, RealEstateFacility realEstateFacility);
    boolean check(String id);
}
