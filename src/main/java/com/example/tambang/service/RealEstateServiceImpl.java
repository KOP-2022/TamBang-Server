package com.example.tambang.service;

import com.example.tambang.configuration.properties.KakaoProperties;
import com.example.tambang.controller.Form;
import com.example.tambang.controller.ResponseVO;
import com.example.tambang.domain.Facility;
import com.example.tambang.domain.FacilityCategory;
import com.example.tambang.domain.RealEstate;
import com.example.tambang.domain.RealEstateFacility;
import com.example.tambang.repository.FacilityRepository;
import com.example.tambang.repository.RealEstateRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.List;

@Service
@Transactional(readOnly = true) //읽기 전용
@RequiredArgsConstructor
public class RealEstateServiceImpl implements RealEstateService {

    private final RealEstateRepositoryImpl realEstateRepository;
    private final FacilityRepository facilityRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KakaoProperties kakaoProperties;

    //매물 등록
    @Override
    @Transactional(readOnly = false)
    public Long register(RealEstate realEstate, String memberEmail){
        if(!validateDuplicateSite(realEstate.getLatitude(), realEstate.getLongitude())){
            // 등록에 error throw
            throw new IllegalStateException();
        }
        realEstateRepository.save(realEstate, memberEmail);
        return realEstate.getId();
    }

    //매물 id 기반 매물 조회
    @Override
    public RealEstate findOneById(Long realEstateId) {
        return realEstateRepository.findOne(realEstateId);
    }

    @Transactional(readOnly = false)
    @Override
    public void registerWithFacility(List<JSONObject> facilities, RealEstate realEstate, String memberEmail, String category_group_code) {
        // 중복 좌표 검사
//        if(!validateDuplicateSite(realEstate.getLatitude(), realEstate.getLongitude())){
//            // 등록에 error throw
//            throw new IllegalStateException();
//        }
        // 회원 정보와 함께 새로운 매물을 등록한다.
        realEstateRepository.save(realEstate, memberEmail);

        // 리스트로 받아온 편의시설을 새로 등록 (매물과 인접한 거리에 있는 편의시설에 대한 등록)
        for (JSONObject object : facilities) {
            Facility facility = new Facility();

            //JSONObject 객체의 정보를 Facility entity 객체에 바인딩
            facility.createFacility(Double.parseDouble(String.valueOf(object.get("x"))), Double.parseDouble(String.valueOf(object.get("y"))),
                    String.valueOf(object.get("address_name")), FacilityCategory.편의점,
                    String.valueOf(object.get("id")), String.valueOf(object.get("phone")),
                    String.valueOf(object.get("place_name")), String.valueOf(object.get("place_url")),
                    String.valueOf(object.get("road_address_name")));
//            System.out.println("facility.toString() = " + facility.toString());

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


    @Override
    public MultiValueMap<String, String> getFacilityParams(Form.RealEstateForm form) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("y", form.getLatitude()); // y는 위도이다.
        params.add("x", form.getLongitude()); // x는 경도이다.
        params.add("page", "0");
        params.add("category_group_code", "CS2");
        params.add("radius", "500"); //근방 500m
        params.add("sort", "distance");

        return params;
    }

    @Transactional(readOnly = false)
    @Override
    public List<JSONObject> getFacilityResponse(MultiValueMap<String, String> params, Form.RealEstateForm form, String email) {
        //category_group_code array
        String[] category_code = {"CS2","FD6","CE7"};

        //편의시설 결과 전부를 담을 map 자료 구조를 저장한다.
        List<JSONObject> Last = new ArrayList<>();

        for (String code : category_code) {
            params.set("category_group_code", code);
            String is_end = "false";
            int page = 0;

            RestTemplate rest = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();

            //헤더 세팅
            httpHeaders.set("Authorization", "KakaoAK " + kakaoProperties.getRestapi());

            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

            //편의시설 결과를 담을 map 자료 구조를 저장한다.
            List<JSONObject> results = new ArrayList<>();

            while (is_end == "false") {
                page += 1;
                params.remove("page");
                params.add("page", Integer.toString(page));

                //한번에 페이지 하나만 불러올 수 있음
                URI targetUrl = UriComponentsBuilder
                        .fromUriString("https://dapi.kakao.com/v2/local/search/category.json")
                        .queryParams(params)
                        .build()
                        .encode()
                        .toUri();
                //post 방식 보내기
                ResponseEntity<String> res = rest.exchange(targetUrl, HttpMethod.POST, httpEntity, String.class);

                //String to json 역할을 하는 method
                JSONParser jsonParser = new JSONParser();

                //res의 body 부분을 담을 곳
                JSONObject body = null;

                //String to json 시도
                try {
                    body = (JSONObject) jsonParser.parse(res.getBody().toString());
                } catch (ParseException e) {
                    logger.info("변환 실패");
                    e.printStackTrace();
                }
                //마지막 페이지 여부 확인
                JSONObject meta = (JSONObject) body.get("meta");

                //documents를 열어보면 JsonArray가 추출됨
                JSONArray docu = (JSONArray) body.get("documents");

                //헤당 페이지가 마지막이 아닐경우
                if (docu.size() != 0) {
                    for (int i = 0; i < docu.size(); i++) {
                        JSONObject temp = (JSONObject) docu.get(i);
                        logger.info("편의점 :: {}", temp);
                        results.add(temp);
                    }
                }
                is_end = String.valueOf(meta.get("is_end"));
                logger.info("is_end :: {}", is_end);
            }
            for (JSONObject result : results) {
                System.out.println("result.toString() = " + result.toString());
            }

            RealEstate realEstate = new RealEstate();
            realEstate.createRealEstate(form.getSigungu(),
                    Double.parseDouble(form.getLatitude()), Double.parseDouble(form.getLongitude()),
                    form.getRoadName(), form.getBuildType(),
                    form.getFloor(), form.getArea(),
                    form.getDealType(), form.getPrice(),
                    form.getDeposit(), form.getMonthlyPay(), form.getDescription(),
                    ""
            );
            //JsonObject 합치기
            Last.addAll(results);

            //매물, 편의 시설 등록 메서드
            registerWithFacility(results, realEstate, email, params.getFirst("category_group_code"));
        }
        return Last;
    }

    @Override
    public List<Facility> getAroundFacilities(Long realEstateId){
        //id 기반으로 매물 조회한 뒤, 매물 주변의 편의시설을 조회한다.
        RealEstate realEstate = realEstateRepository.findOne(realEstateId);
        List<Facility> facilities = realEstateRepository.findAroundFacilities(realEstate);

        //조회 결과를 controller로 반환
        return facilities;
    }

    public List<ResponseVO.RealEstateVO> getAroundRealEstates(double latitude, double longitude, double radius){
        List<RealEstate> realEstates = realEstateRepository.findAll();
        List<ResponseVO.RealEstateVO> inRangeRealEstates = new ArrayList<>();

        for (RealEstate realEstate : realEstates) {
            double distance = getDistance(latitude, longitude, realEstate.getLatitude(), realEstate.getLongitude());
//            System.out.println("realEstate.getLatitude() = " + realEstate.getLatitude() + " longitude: " + realEstate.getLongitude());
//            System.out.println("distance = " + distance + ", " + radius);
            // radius 범위 이내의 매물을 리스트에 추가한다.
            if(distance <= radius){
                ResponseVO.RealEstateVO realEstateVO = new ResponseVO.RealEstateVO(realEstate.getId(), realEstate.getLatitude(), realEstate.getLongitude());
                inRangeRealEstates.add(realEstateVO);
            }
        }

        return inRangeRealEstates;
    }

    private double getDistance(double aLat, double aLon, double bLat, double bLon){
        int radius = 6371; //지구의 반지름

        double dLat = rad(bLat - aLat);
        double dLon = rad(bLon - aLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(aLon) * Math.cos(bLon);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dDistance = radius * c;

        //미터 단위로 변환
        dDistance *= 1000;
        return dDistance;
    }

    private double rad(double x){
        return x * 3.14159 / 180.0;
    }

    private boolean validateDuplicateSite(double latitude, double longitude){
        List<RealEstate> realEstates = realEstateRepository.findAll();

        for(RealEstate realEstate : realEstates){
            // 일단 같은 위도와 경도의 매물 중복 등록 불가능하도록 설정
            if(realEstate.getLatitude() == latitude && realEstate.getLongitude() == longitude){
                System.out.println("realEstate.getLatitude() = " + realEstate.getLatitude());
                System.out.println("latitude = " + latitude);
                System.out.println("realEstate.getLongitude() = " + realEstate.getLongitude());
                System.out.println("latitude = " + longitude);
                return false;
            }
        }

        return true;
    }
}

