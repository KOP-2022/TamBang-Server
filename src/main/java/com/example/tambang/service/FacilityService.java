package com.example.tambang.service;

import org.springframework.stereotype.Service;

@Service
public class FacilityService {

    //두 좌표 사이의 거리를 구하는 메서드
    public double getDistance(double aLat, double aLon, double bLat, double bLon){
        int radius = 6371; //지구의 반지름

        double dLat = rad(bLat - aLat);
        double dLon = rad(bLon - aLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(aLon) * Math.cos(bLon);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dDistance = radius * c;

        //미터 단위로 변환
        dDistance *= 1000;
        return dDistance;
    }

    private double rad(double x){
        return x * 3.14159 / 180.0;
    }
}
