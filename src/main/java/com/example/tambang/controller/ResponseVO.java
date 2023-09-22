package com.example.tambang.controller;

import com.example.tambang.domain.Address;
import com.example.tambang.domain.BuildInfo;
import com.example.tambang.domain.FacilityCategory;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class ResponseVO {

    private Map<String, Object> data = new HashMap<>();
    private boolean success;

    //불변 객체로 만들기 위해 생성자로만 초기화한다.
    @Getter
    public static class RealEstateResponse{
        private Address address;
        private BuildInfo buildInfo;
        private String description;
        private String memberEmail;

        public RealEstateResponse(Address address, BuildInfo buildInfo, String description, String memberEmail) {
            this.address = address;
            this.buildInfo = buildInfo;
            this.description = description;
            this.memberEmail = memberEmail;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class FacilityVO{
        private Long id;
        //편의시설 좌표 정보
        private Double longitude;
        private Double latitude;

        private String addressName;
        //enum type field가 문자열 그대로 입력되도록 한다.
        private FacilityCategory categoryGroupName;
        private String kakaoId;
        private String phone;
        private String placeName;
        private String placeUrl;
        private String roadAddressName;
    }

    @Getter
    @AllArgsConstructor
    public static class FacilityResponse{
        private boolean success;
        private List<FacilityVO> data;
    }
}
