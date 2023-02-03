package com.example.tambang.controller;

import com.example.tambang.configuration.properties.KakaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facility/*")
public class FacilityController {

    private final KakaoProperties kakaoProperties;

    @PostMapping("/search")
    public String search(@RequestBody FacilityForm form){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("x", form.getLatitude());
        params.add("y", form.getLongitude());
        params.add("category_group_code", "CS2");
        params.add("radius", "100"); //근방 100미터의 편의점을 조회

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
        return mono.block();
    }

}
