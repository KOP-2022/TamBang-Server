package com.example.tambang.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class RealEstate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member; // member 1 : N real estate

    //위치 정보
    private String sigungu;
    private String roadName;
    private String buildType;
    
    //건물 정보
    private int floor; //층
    private Float area; //면적(제곱미터)
    
    private String dealType;
    
    private int price; //매매금
    private int deposit; //계약금(보증금)

    @Column(name = "monthly_pay")
    private int monthlyPay;

    private String description;
    private String image; //이미지는 어떻게 처리할지 고민중
}
