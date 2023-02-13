package com.example.tambang.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Facility {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //편의시설 좌표 정보
    private Double longitude;
    private Double latitude;

    private String addressName;
    //enum type field가 문자열 그대로 입력되도록 한다.
    @Enumerated(EnumType.STRING)
    private FacilityCategory categoryGroupName;
    private String kakaoId;
    private String phone;
    private String placeName;
    private String placeUrl;
    private String roadAddressName;

    public void createFacility(Double longitude, Double latitude, String addressName, FacilityCategory categoryGroupName, String kakaoId, String phone, String placeName, String placeUrl, String roadAddressName) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.addressName = addressName;
        this.categoryGroupName = categoryGroupName;
        this.kakaoId = kakaoId;
        this.phone = phone;
        this.placeName = placeName;
        this.placeUrl = placeUrl;
        this.roadAddressName = roadAddressName;
    }

}
