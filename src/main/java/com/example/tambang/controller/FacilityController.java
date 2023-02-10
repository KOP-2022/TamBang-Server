package com.example.tambang.controller;

import com.example.tambang.configuration.properties.KakaoProperties;
import lombok.RequiredArgsConstructor;
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
    public Map search(@RequestBody FacilityForm form){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("y", form.getLatitude()); // y는 위도이다.
        params.add("x", form.getLongitude()); // x는 경도이다.
        params.add("category_group_code", "CS2");
        params.add("radius", "2000"); //근방 2km의 편의점을 조회


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + kakaoProperties.getRestapi());

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        URI targetUrl = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/search/category.json")
                .queryParams(params)
                .build()
                .encode()
                .toUri();

        ResponseEntity<Map> result = restTemplate.exchange(targetUrl, HttpMethod.POST,httpEntity,Map.class);
        logger.info("responseEntity: {}", result);
        return result.getBody();



    }

}
