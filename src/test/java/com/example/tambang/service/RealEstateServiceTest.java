package com.example.tambang.service;

import com.example.tambang.domain.Member;
import com.example.tambang.domain.RealEstate;
import com.example.tambang.repository.RealEstateRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RealEstateServiceTest {

    @Autowired RealEstateService realEstateService;
    @Autowired RealEstateRepository realEstateRepository;

    @Test
    @Rollback(false)
    public void 매물등록() throws Exception {
        //given
        RealEstate realEstate = new RealEstate();
        realEstate.createRealEstate("서울시 노원구","광운로 15길 14", "빌라", 3, 4.1, "전세",0,1000000000,0,"집이 죠습니다","/C:/Users/actgo/Pictures/투게더.jpg");

        Member member = new Member();
        member.createMember("test_id@kw.ac.kr", "test_passwd", "kang", "kkkdh", "010-6666-5555");

        //when
        Long savedId = realEstateService.register(realEstate, "test_id@kw.ac.kr");

        //then
        assertEquals(realEstate, realEstateRepository.findOne(savedId));
    }


}