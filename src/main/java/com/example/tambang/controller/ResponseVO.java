package com.example.tambang.controller;

import com.example.tambang.domain.FacilityCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResponse{
        private Boolean success;
        private HashMap<String, String> data = new HashMap<>();
    }

    @Getter
    public static class MemberResponse{
        private Boolean success;
        private Map<String, String> data = new HashMap<>();

        public MemberResponse(Boolean success, String email, String name, String nickname, String phoneNumber) {
            this.success = success;
            this.data.put("email", email);
            this.data.put("name", name);
            this.data.put("nickname", nickname);
            this.data.put("phone_number", phoneNumber);
        }

        public MemberResponse(Boolean success){
            this.success = success;
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
    @Getter
    @AllArgsConstructor
    public static class RealEstateVO{
        private Long id;
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @AllArgsConstructor
    public static class RealEstateListResponse{
        private boolean success;
        private List<RealEstateVO> data;
    }
}
