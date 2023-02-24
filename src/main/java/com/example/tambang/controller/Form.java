package com.example.tambang.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public abstract class Form {
    //form DTO들을 static class로 선언해서 외부에서 사용 가능하도록 한다.
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class FacilityForm {
        //위도와 경도
        private String latitude;  // y 위도
        private String longitude; // x 경도
        private String address_name;
        private String category_group_name;
        private String id;
        private String phone;
        private String place_name;
        private String place_url;
        private String road_address_name;

    }

    @RequiredArgsConstructor
    @Getter @Setter
    public static class LoginForm { //DTO for login

        private String email;
        private String password;
    }

    @RequiredArgsConstructor
    @Getter @Setter
    public static class MemberForm { //사실상 DTO(Data Transfer Object)와 같다.
        private String email;
        private String password;
        private String name;
        private String nickname;
        private String phoneNumber;
    }

    @RequiredArgsConstructor
    @Getter
    @Setter
    public static class RealEstateForm {

        private String latitude;
        private String longitude;
        private String sigungu;
        private String roadName;
        private String buildType;
        private Long floor;
        private Double area;
        private String dealType;
        private Long price;
        private Long deposit;
        private Long monthlyPay;
        private String description;
        private String memberId;

        @Override
        public String toString() {
            return "RealEstateForm{" +
                    "latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", sigungu='" + sigungu + '\'' +
                    ", roadName='" + roadName + '\'' +
                    ", buildType='" + buildType + '\'' +
                    ", floor=" + floor +
                    ", area=" + area +
                    ", dealType='" + dealType + '\'' +
                    ", price=" + price +
                    ", deposit=" + deposit +
                    ", monthlyPay=" + monthlyPay +
                    ", description='" + description + '\'' +
                    ", memberId='" + memberId + '\'' +
                    '}';
        }
    }

}
