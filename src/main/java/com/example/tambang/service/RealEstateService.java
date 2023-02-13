package com.example.tambang.service;

import com.example.tambang.domain.RealEstate;
import org.json.simple.JSONObject;

import java.util.List;

public interface RealEstateService {

    Long register(RealEstate realEstate, String member_id);
    RealEstate findOne(Long realEstateId);
    void registerWithFacility(List<JSONObject> facilities, RealEstate realEstate, String memberEmail);
}
