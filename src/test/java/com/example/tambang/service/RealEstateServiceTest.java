package com.example.tambang.service;

import com.example.tambang.domain.Member;
import com.example.tambang.domain.RealEstate;
import com.example.tambang.repository.MemberRepository;
import com.example.tambang.repository.RealEstateRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RealEstateServiceTest {

    @Autowired
    RealEstateService realEstateService;
    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(false)
    public void 매물등록() throws Exception {
        //given
        RealEstate realEstate = new RealEstate();
        double kw_latitude = 37.61905576090399;
        double kw_longitude = 127.0582715974131;
        realEstate.createRealEstate("서울시 노원구", kw_latitude, kw_longitude, "광운로 15길 14",
                "빌라", 3L, 4.1, "전세",0L,1000000000L,0L,
                "집이 죠습니다","/C:/Users/actgo/Pictures/투게더.jpg");

        Member member = new Member();
        member.createMember("test_id@kw.ac.kr", "test_passwd", "kang", "kkkdh", "010-6666-5555");

        //when
        Long savedId = realEstateService.register(realEstate, "test_id@kw.ac.kr");

        //then
        assertEquals(realEstate, realEstateRepository.findOne(savedId));
    }

    @Test
    @Rollback
    public void 매물조회() throws Exception{
        //given
        RealEstate realEstate = new RealEstate();
        double test_latitude = 37.61905576090399;
        double test_longitude = 127.0582715974131;
        realEstate.createRealEstate("서울시 노원구", test_latitude, test_longitude, "광운로 15길 14",
                "빌라", 3L, 4.1, "전세",0L,1000000000L,0L,
                "집이 죠습니다","/C:/Users/actgo/Pictures/투게더.jpg");

        Member member = new Member();
        member.createMember("test_id@kw.ac.kr", "test_passwd", "kang", "kkkdh", "010-6666-5555");

        //when
        memberRepository.save(member);
        realEstate.setOwner(member);
        realEstateRepository.save(realEstate, member.getEmail());
        RealEstate findRealEstate = realEstateService.findOneById(realEstate.getId());

        //then
        assertThat(findRealEstate).isEqualTo(realEstate);
    }

    @Test
    @Rollback
    public void 잘못된_매물_조회() throws Exception{
        //given
        RealEstate realEstate = new RealEstate();
        double test_latitude = 37.61905576090399;
        double test_longitude = 127.0582715974131;
        realEstate.createRealEstate("서울시 노원구", test_latitude, test_longitude, "광운로 15길 14",
                "빌라", 3L, 4.1, "전세",0L,1000000000L,0L,
                "집이 죠습니다","/C:/Users/actgo/Pictures/투게더.jpg");

        Member member = new Member();
        member.createMember("test_id@kw.ac.kr", "test_passwd", "kang", "kkkdh", "010-6666-5555");

        //when
        memberRepository.save(member);
        realEstate.setOwner(member);
        realEstateRepository.save(realEstate, member.getEmail());
        RealEstate findRealEstate = realEstateService.findOneById(0L); //id는 1부터 시작하기 때문에, 0은 있을 수 없다.

        //then
        assertThat(findRealEstate).isNotEqualTo(member); //하나는 null이더라도, 동등성 판단이 가능한듯
    }

}