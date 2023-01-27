package com.example.tambang.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Facility {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //enum type field가 문자열 그대로 입력되도록 한다.
    @Enumerated(EnumType.STRING)
    private FacilityType facilityType;

    //편의시설 좌표 정보
    private Float longitude;
    private Float langitude;
}
