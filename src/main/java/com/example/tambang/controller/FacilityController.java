package com.example.tambang.controller;

import com.example.tambang.configuration.properties.KakaoProperties;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facility/*")
public class FacilityController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KakaoProperties kakaoProperties;
    /*
    @PostMapping("/search")
    public String as(@RequestBody FacilityForm form){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("y", form.getLatitude()); // y는 위도이다.
        params.add("x", form.getLongitude()); // x는 경도이다.
        params.add("category_group_code", "CS2");
        params.add("radius", "2000"); //근방 2km의 편의점을 조회

        //카카오 로컬 api로부터 편의시설 정보를 얻어온다.
        Mono<String> mono = WebClient.builder().baseUrl("https://dapi.kakao.com")
                .build().get()
                .uri(builder -> builder.path("/v2/local/search/category.json")
                        .queryParams(params)
                        .build()
                )
                .header("Authorization", "KakaoAK " + kakaoProperties.getRestapi())
                .exchangeToMono(response -> {
                    return response.bodyToMono(String.class);
                });
        System.out.println("mono =" + mono.block());
        return mono.block();
    }
    */
    @PostMapping("/search")
    public String search(@RequestBody FacilityForm form){

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("y", form.getLatitude()); // y는 위도이다.
        params.add("x", form.getLongitude()); // x는 경도이다.
        params.add("page", "1");
        params.add("category_group_code", "CS2");
        params.add("radius", "2000"); //근방 2km의 편의점을 조회

        RestTemplate rest = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();


        //헤더 세팅
        httpHeaders.set("Authorization", "KakaoAK " + kakaoProperties.getRestapi());

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

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
        if(docu.size() != 0) {
            for (int i = 0; i < docu.size(); i++) {
                JSONObject temp = (JSONObject) docu.get(i);
                logger.info("편의점 :: {}", temp.get("place_name"));
            }
        }
        logger.info("is_end :: {}", meta.get("is_end"));
        //Object obj = meta.get("is_end");
        return res.getBody();
        }




    }

