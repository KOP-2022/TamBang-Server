package com.example.tambang.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class RealEstate {
    //autoIncrement
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "real_estate_id")
    private Long id;

    @ManyToOne
    private Member member; // member 1 : N real estate

    //위치 정보
    private String sigungu;
    private String roadName;
    private String buildType;
    
    //건물 정보
    private int floor; //층
    private double area; //면적(제곱미터)
    
    private String dealType;

    private int price; //매매금
    private int deposit; //계약금(보증금)

    @Column(name = "monthly_pay")
    private int monthlyPay;

    private String description;
    private String image; //이미지는 어떻게 처리할지 고민중

    public void CreateRealEstate(String sigungu, String roadName, String buildType, int floor, double area, String dealType, int price, int deposit, int monthlyPay, String description, String image) {
        this.sigungu = sigungu;
        this.roadName = roadName;
        this.buildType = buildType;
        this.floor = floor;
        this.area = area;
        this.dealType = dealType;
        this.price = price;
        this.deposit = deposit;
        this.monthlyPay = monthlyPay;
        this.description = description;
        this.image = image;
    }






}
