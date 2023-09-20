package com.example.tambang.service;

import com.example.tambang.controller.ResponseVO;
import com.example.tambang.domain.Facility;
import com.example.tambang.domain.Member;
import com.example.tambang.domain.RealEstate;
import com.example.tambang.domain.RealEstateFacility;
import com.example.tambang.dto.MemberCreateRequestDto;
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

import javax.persistence.Entity;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

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
    MemberService memberService;
    @Autowired
    EntityManager em;

    private static String TEST_SIGUNGU = "서울시 노원구";
    private static String TEST_ROAD_NAME = "광운로 15길 14";
    private static String TEST_BUILD_TYPE = "빌라";
    private static Long TEST_FLOOR = 3L;
    private static Double TEST_AREA = 4.1;
    private static String TEST_DEAL_TYPE = "전세";
    private static Long TEST_PRICE = 0L;
    private static Long TEST_DEPOSIT = 1000000000L;
    private static Long TEST_MONTHLY_PAY = 0L;
    private static String TEST_DESCRIPTION = "집이 죠습니다.";
    private static String TEST_IMAGE = "/C:/Users/actgo/Pictures/투게더.jpg";
    private static String TEST_EMAIL = "test_email";
    private static String TEST_PASSWORD = "test_passwd";
    private static String TEST_NAME = "kang";
    private static String TEST_NICKNAME = "kkkdh";
    private static String TEST_PHONE_NUMBER = "010-0000-1111";

    @Test
    @Rollback
    public void 매물등록() throws Exception {
        //given
        RealEstate realEstate = new RealEstate();
        double kw_latitude = 37.61905576090399;
        double kw_longitude = 127.0582715974131;
        realEstate.createRealEstate(TEST_SIGUNGU, kw_latitude, kw_longitude, TEST_ROAD_NAME,
                TEST_BUILD_TYPE, TEST_FLOOR, TEST_AREA, TEST_DEAL_TYPE,TEST_PRICE,TEST_DEPOSIT,TEST_MONTHLY_PAY,
                TEST_DESCRIPTION, TEST_IMAGE);

        MemberCreateRequestDto requestDto = new MemberCreateRequestDto(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_NICKNAME, TEST_PHONE_NUMBER);
        memberService.join(requestDto);

        //when
        Long savedId = realEstateService.register(realEstate, TEST_EMAIL);

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
        realEstate.createRealEstate(TEST_SIGUNGU, test_latitude, test_longitude, TEST_ROAD_NAME,
                TEST_BUILD_TYPE, TEST_FLOOR, TEST_AREA, TEST_DEAL_TYPE,TEST_PRICE,TEST_DEPOSIT,TEST_MONTHLY_PAY,
                TEST_DESCRIPTION, TEST_IMAGE);

        MemberCreateRequestDto requestDto = new MemberCreateRequestDto(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_NICKNAME, TEST_PHONE_NUMBER);
        String memberEmail = memberService.join(requestDto);
        Member member = memberService.findByEmail(memberEmail).get();

        //when
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
        realEstate.createRealEstate(TEST_SIGUNGU, test_latitude, test_longitude, TEST_ROAD_NAME,
                TEST_BUILD_TYPE, TEST_FLOOR, TEST_AREA, TEST_DEAL_TYPE,TEST_PRICE,TEST_DEPOSIT,TEST_MONTHLY_PAY,
                TEST_DESCRIPTION, TEST_IMAGE);

        MemberCreateRequestDto requestDto = new MemberCreateRequestDto(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_NICKNAME, TEST_PHONE_NUMBER);
        String memberEmail = memberService.join(requestDto);
        Member member = memberService.findByEmail(memberEmail).get();

        //when
        realEstate.setOwner(member);
        realEstateRepository.save(realEstate, member.getEmail());
        RealEstate findRealEstate = realEstateService.findOneById(0L); //id는 1부터 시작하기 때문에, 0은 있을 수 없다.

        //then
        assertThat(findRealEstate).isNotEqualTo(member); //하나는 null이더라도, 동등성 판단이 가능한듯
    }

    @Test
    @Rollback
    public void 매물_편의시설_조회() throws Exception{
        //given
        RealEstate realEstate = new RealEstate();
        em.persist(realEstate);

        Facility facility = new Facility();
        em.persist(facility);

        RealEstateFacility rf = new RealEstateFacility();
        rf.setRealEstate(realEstate);
        rf.setFacility(facility);
        em.persist(rf);

        //when
        em.flush();
        em.clear();

        List<Facility> facilities = realEstateService.getAroundFacilities(realEstate.getId());

        //then
        assertThat(facilities.size()).isEqualTo(1);
        assertThat(facilities.get(0)).isInstanceOf(Facility.class);
        assertThat(facilities.get(0).getId()).isEqualTo(facility.getId());
    }

    @Test
    @Rollback
    public void 기준좌표_500m내의_매물조회() throws Exception{
        //given
        RealEstate realEstate = new RealEstate();
        //비마관
        double des_latitude = 37.61963159909614;
        double des_longitude = 127.05985783472804;
        //광운대 정문
        double kw_latitude = 37.61905576090399;
        double kw_longitude = 127.0582715974131;

        double radius = 500;

        realEstate.createRealEstate(TEST_SIGUNGU, kw_latitude, kw_longitude, TEST_ROAD_NAME,
                TEST_BUILD_TYPE, TEST_FLOOR, TEST_AREA, TEST_DEAL_TYPE,TEST_PRICE,TEST_DEPOSIT,TEST_MONTHLY_PAY,
                TEST_DESCRIPTION, TEST_IMAGE);

        em.persist(realEstate);

        //when
        List<ResponseVO.RealEstateVO> aroundRealEstates = realEstateService.getAroundRealEstates(des_latitude, des_longitude, radius);

        //then
        assertThat(aroundRealEstates.size()).isNotEqualTo(0);
    }
}