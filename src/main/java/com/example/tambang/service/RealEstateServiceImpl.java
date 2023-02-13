package com.example.tambang.service;

import com.example.tambang.domain.Facility;
import com.example.tambang.domain.FacilityCategory;
import com.example.tambang.domain.RealEstate;
import com.example.tambang.domain.RealEstateFacility;
import com.example.tambang.repository.FacilityRepository;
import com.example.tambang.repository.RealEstateRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //읽기 전용
@RequiredArgsConstructor
public class RealEstateServiceImpl implements RealEstateService{

    private final RealEstateRepositoryImpl realEstateRepository;
    private final FacilityRepository facilityRepository;

    //매물 등록
    @Override
    @Transactional(readOnly = false)
    public Long register(RealEstate realEstate, String member_id){
        realEstateRepository.save(realEstate, member_id);
        return realEstate.getId();
    }

    //매물 id 기반 매물 조회
    @Override
    public RealEstate findOne(Long realEstateId){
        return realEstateRepository.findOne(realEstateId);
    }

    @Transactional(readOnly = false)
    @Override
    public void registerWithFacility(List<JSONObject> facilities, RealEstate realEstate, String memberEmail){
        //회원 정보와 함께 새로운 매물을 등록한다.
        realEstateRepository.save(realEstate, memberEmail);

        //리스트로 받아온 편의시설을 새로 등록 (매물과 인접한 거리에 있는 편의시설에 대한 등록)
        for(JSONObject object : facilities) {
            Facility facility = new Facility();

            //JSONObject 객체의 정보를 Facility entity 객체에 바인딩
            facility.createFacility(Double.parseDouble(String.valueOf(object.get("x"))), Double.parseDouble(String.valueOf(object.get("y"))),
                    String.valueOf(object.get("address_name")), FacilityCategory.편의점,
                    String.valueOf(object.get("id")), String.valueOf(object.get("phone")),
                    String.valueOf(object.get("place_name")), String.valueOf(object.get("place_url")),
                    String.valueOf(object.get("road_address_name")));
            System.out.println("facility.toString() = " + facility.toString());

            /*
            중복되는 편의시설은 추가하면 안됨
            중복 여부는 kakao_id로 판단
            연관관계(500m 이내의 거리 여부 판단)는 RealEstateFacility entity로 관리한다.
            */
            if (facilityRepository.check(facility.getKakaoId()))
                continue;

            //부동산과 편의시설의 연관관계를 매핑할 entity 객체를 생성한다.
            RealEstateFacility realEstateFacility = new RealEstateFacility();
            realEstateFacility.setRealEstate(realEstate);
            realEstateFacility.setFacility(facility);

            facilityRepository.enrollWithRealEstate(facility, realEstateFacility);
        }
    }

}
