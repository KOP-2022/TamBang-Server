package com.example.tambang.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
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
}
