package com.example.tambang.service;

import com.example.tambang.controller.Form;
import com.example.tambang.domain.RealEstate;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface RealEstateService {

    Long register(RealEstate realEstate, String member_id);
    
    RealEstate findOne(Long realEstateId);    

    RealEstate findOneById(Long realEstateId);

    void registerWithFacility(List<JSONObject> facilities, RealEstate realEstate, String memberEmail);

    MultiValueMap<String, String> getFacilityParams(Form.RealEstateForm form);

    List<JSONObject> getFacilityResponse(MultiValueMap<String, String> params, Form.RealEstateForm form, String email);

}


