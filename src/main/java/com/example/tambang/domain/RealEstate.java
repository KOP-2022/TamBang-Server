package com.example.tambang.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class RealEstate {
    //autoIncrement
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitude;
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // member 1 : N real estate

    //위치 정보
    private String sigungu;
    private String roadName;
    private String buildType;
    
    //건물 정보
    private Long floor; //층
    private Double area; //면적(제곱미터)
    private String dealType; //거래 종류(매매, 전세, 월세), 추후에 enum type으로 변경할듯
    private Long price; //매매금
    private Long deposit; //계약금(보증금)

    @Column(name = "monthly_pay")
    private Long monthlyPay;

    private String description;
    private String image; //이미지는 어떻게 처리할지 고민중

    public void createRealEstate(String sigungu, Double latitude, Double longitude, String roadName, String buildType, Long floor, Double area, String dealType, Long price, Long deposit, Long monthlyPay, String description, String image) {
        this.sigungu = sigungu;
        this.latitude = latitude;
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return "RealEstate{" +
                "id=" + id +
                ", member=" + member +
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
                ", image='" + image + '\'' +
                '}';
    }
    //집주인을 설정
    public void setOwner(Member member){
        this.member = member;
    }
}
