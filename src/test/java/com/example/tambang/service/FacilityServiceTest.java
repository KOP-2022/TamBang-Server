package com.example.tambang.service;

import com.example.tambang.domain.Facility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FacilityServiceTest {

    //테스트를 위한 필드 주입
    @Autowired
    FacilityService facilityService;

    @Test
    public void 거리계산(){
        Facility facility1 = new Facility();
        Facility facility2 = new Facility();

//        facility1.createFacility
        //광운대 정문
        double kw_latitude = 37.61905576090399;
        double kw_longitude = 127.0582715974131;

        //비마관
        double des_latitude = 37.61963159909614;
        double des_longitude = 127.05985783472804;

        System.out.println(facilityService.getDistance(kw_latitude, kw_longitude, des_latitude, des_longitude));
    }
}
