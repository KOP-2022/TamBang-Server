package com.example.tambang.service;

import com.example.tambang.domain.RealEstate;
import com.example.tambang.repository.RealEstateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) //읽기 전용
@RequiredArgsConstructor
public class RealEstateService {

    private final RealEstateRepository realEstateRepository;

    //매물 등록
    @Transactional(readOnly = false)
    public Long register(RealEstate realEstate, String member_id){
        realEstateRepository.save(realEstate, member_id);
        return realEstate.getId();
    }
    //매물 id 기반 매물 조회
    public RealEstate findOne(Long realEstateId){
        return realEstateRepository.findOne(realEstateId);
    }

}
