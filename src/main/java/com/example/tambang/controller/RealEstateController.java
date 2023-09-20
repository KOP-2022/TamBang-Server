package com.example.tambang.controller;

import com.example.tambang.domain.Facility;
import com.example.tambang.domain.RealEstate;
import com.example.tambang.service.RealEstateServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Tag(name = "main api", description = "main")
@RestController
@RequiredArgsConstructor
public class RealEstateController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RealEstateServiceImpl realEstateService;

    @GetMapping("/test")
    @Operation(summary = "test", description = "test")
    public String test(){
        return "test";
    }

    //@RequestPart 어노테이션을 활용해 여러 유형의 request body를 한 번에 매핑할 수 있다.
    //매물 등록 요청
    @PostMapping("/real-estates")
    @Operation(summary = "search", description = "편의시설 정보 검색")
    public List<JSONObject> search(@RequestPart(name = "form", required=true)  Form.RealEstateForm form,
                                   @RequestPart(name = "file", required=false) MultipartFile file, @RequestPart String email) {
        //편의 시설 정보 요청보내기, 가공해서 가져오기
        return realEstateService.getFacilityResponse(realEstateService.getFacilityParams(form), form, email);
    }


    //부동산 id 기반한 편의시설 조회
    @GetMapping("/real-estates/{real-estate-id}")
    public ResponseVO.RealEstateResponse getRealEstateInfo(@PathVariable(name="real-estate-id") Long realEstateId){
        HashMap<String, Object> map = new HashMap<>();

        RealEstate findRealEstate = realEstateService.findOneById(realEstateId);

        Address address = new Address(findRealEstate.getSigungu(), findRealEstate.getRoadName());
        BuildInfo buildInfo = new BuildInfo(findRealEstate.getBuildType(), findRealEstate.getFloor(), findRealEstate.getArea(), findRealEstate.getDealType(),
                findRealEstate.getPrice(), findRealEstate.getDeposit(), findRealEstate.getMonthlyPay());

        return new ResponseVO.RealEstateResponse(address, buildInfo, findRealEstate.getDescription(), findRealEstate.getMember().getEmail());
    }

    @GetMapping("/real-estates/{real-estate-id}/facilities")
    public ResponseVO.FacilityResponse getRealEstateFacilityInfo(@PathVariable(name="real-estate-id") Long realEstateId){
        List<ResponseVO.FacilityVO> facilityVoList = new ArrayList<>();
        System.out.println("realEstateId = " + realEstateId);
        List<Facility> facilities = realEstateService.getAroundFacilities(realEstateId);

        for (Facility facility : facilities) {
            ResponseVO.FacilityVO facilityVO = new ResponseVO.FacilityVO(
                    facility.getId(),
                    facility.getLongitude(),
                    facility.getLatitude(),
                    facility.getAddressName(),
                    facility.getCategoryGroupName(),
                    facility.getKakaoId(),
                    facility.getPhone(),
                    facility.getPlaceName(),
                    facility.getPlaceUrl(),
                    facility.getRoadAddressName()
                    );
            facilityVoList.add(facilityVO);
        }
        ResponseVO.FacilityResponse res = new ResponseVO.FacilityResponse(true, facilityVoList);

        return res;
    }
}
