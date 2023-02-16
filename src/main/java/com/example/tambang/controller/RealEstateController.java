package com.example.tambang.controller;

import com.example.tambang.configuration.properties.KakaoProperties;
import com.example.tambang.domain.RealEstate;
import com.example.tambang.service.RealEstateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/real-estates/*")
public class RealEstateController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KakaoProperties kakaoProperties;
    private final RealEstateServiceImpl realEstateService;

    //@RequestPart 어노테이션을 활용해 여러 유형의 request body를 한 번에 매핑할 수 있다.
    @PostMapping("/form")
    public void search(@RequestPart(name = "form", required=true)  Form.RealEstateForm form, @RequestPart(name = "file", required=false) MultipartFile file, @RequestPart String email) {
        String is_end = "false";

        System.out.println("form.toString() = " + form.toString());
        
        int page = 0;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("y", form.getLatitude()); // y는 위도이다.
        params.add("x", form.getLongitude()); // x는 경도이다.
        params.add("page", "0");
        params.add("category_group_code", "CS2");
        params.add("radius", "500"); //근방 500m
        params.add("sort", "distance");

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

//        realEstateService.register(realEstate, "kkkdh@kw");
        realEstateService.registerWithFacility(results, realEstate, email);
    }

    @GetMapping("/{real-estate-id}")
    public ResponseVO.RealEstateResponse getRealEstateInfo(@PathVariable(name="real-estate-id") Long realEstateId){
        HashMap<String, Object> map = new HashMap<>();

        RealEstate findRealEstate = realEstateService.findOne(realEstateId);

        Address address = new Address(findRealEstate.getSigungu(), findRealEstate.getRoadName());
        BuildInfo buildInfo = new BuildInfo(findRealEstate.getBuildType(), findRealEstate.getFloor(), findRealEstate.getArea(), findRealEstate.getDealType(),
                findRealEstate.getPrice(), findRealEstate.getDeposit(), findRealEstate.getMonthlyPay());

        return new ResponseVO.RealEstateResponse(address, buildInfo, findRealEstate.getDescription(), findRealEstate.getMember().getEmail());
    }
}
