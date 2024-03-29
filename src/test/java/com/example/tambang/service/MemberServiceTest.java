package com.example.tambang.service;

import com.example.tambang.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberServiceImpl memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    private static String TEST_EMAIL = "test_email";
    private static String TEST_PASSWORD = "test_passwd";
    private static String TEST_NAME = "kang";
    private static String TEST_NICKNAME = "kkkdh";
    private static String TEST_PHONE_NUMBER = "010-0000-1111";

    @Test
    @DisplayName("회원등록")
    public void 회원등록() {
        //given
        Member member = new Member();
        member.createMember(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_NICKNAME, TEST_PHONE_NUMBER);

        //when
        String memberId = memberService.join(member);

        //then
        assertThat(memberId).isEqualTo(TEST_EMAIL);
    }

    @Test(expected = IllegalStateException.class)
    @DisplayName("중복된 아이디의 회원 등록")
    public void 중복_회원_등록() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        String test_fail_name = "kong";
        member1.createMember(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_NICKNAME, TEST_PHONE_NUMBER);
        member2.createMember(TEST_EMAIL, TEST_PASSWORD, test_fail_name, TEST_NICKNAME, TEST_PHONE_NUMBER);

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        List<Member> memberList = memberService.findAll();
        Assertions.assertThat(memberList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인 테스트")
    public void 로그인() {
        //given
        Member member = new Member();
        String encodedPassword = passwordEncoder.encode(TEST_PASSWORD);

        member.createMember(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_NICKNAME, TEST_PHONE_NUMBER);

        member.grantAuthority("USER");
        memberService.join(member);

        //when
        String jwt = memberService.login(TEST_EMAIL, TEST_PASSWORD);

        //then
        boolean isMatch = passwordEncoder.matches(TEST_PASSWORD, encodedPassword);
        assertThat(isMatch).isEqualTo(true);
        assertThat(jwt).isNotEqualTo("");
    }

    @Test
    @DisplayName("로그인 실패하는 경우")
    public void 로그인_실패() {
        //given
        Member member = new Member();
        String encodedPassword = passwordEncoder.encode(TEST_PASSWORD);

        member.createMember(TEST_EMAIL, encodedPassword, TEST_NAME, TEST_NICKNAME, TEST_PHONE_NUMBER);
        memberService.join(member);

        //when
        String jwt = memberService.login(TEST_EMAIL, TEST_PASSWORD);

        //then
        assertThat(jwt).isEqualTo(""); // 로그인 실패한 경우 빈 토큰이 발급된다.
    }
}
