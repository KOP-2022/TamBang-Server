package com.example.tambang.service;

import com.example.tambang.domain.Member;
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

    @Test
    @DisplayName("회원등록")
    public void 회원등록() throws Exception {
        System.out.println("test start!!");
        Member member = new Member();
        member.createMember("test_id", "test_passwd", "kang", "kkkdh", "010-6666-5555");

        String memberId = memberService.join(member);
        assertThat(memberId).isEqualTo("test_id");
    }

    @Test(expected = IllegalStateException.class)
    @DisplayName("중복된 아이디의 회원 등록")
    public void 중복_회원_등록() throws Exception {
        //given
        Member member1 = new Member();
        Member member2 = new Member();
        member1.createMember("test_id", "test_passwd", "kang", "kkkdh", "010-6666-5555");
        member2.createMember("test_id", "test_passwd", "kong", "kkkdh1", "010-6666-5555");

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        Assert.fail("예외가 발생해야 한다.");
    }

    @Test
    @DisplayName("로그인 테스트")
    public void 로그인() throws Exception{
        //given
        Member member = new Member();
        String testPassword = "test_passwd";
        String encodedPassword = passwordEncoder.encode(testPassword);
        member.createMember("test_id@kw.ac.kr", testPassword, "kang", "kkkdh", "010-6666-5555");
        member.grantAuthority("USER");
//        memberService.join(member);
        memberService.join(member);
        //when
        String jwt = memberService.login("test_id@kw.ac.kr", testPassword);

        //then
        boolean isMatch = passwordEncoder.matches(testPassword, encodedPassword);
        assertThat(isMatch).isEqualTo(true);
        assertThat(jwt).isNotEqualTo("");
    }

    @Test
    @DisplayName("로그인 실패하는 경우")
    public void 로그인_실패() {
        //given
        Member member = new Member();
        String testPassword = "test_passwd";
        String encodedPassword = passwordEncoder.encode(testPassword);

        member.createMember("test_id@kw.ac.kr", encodedPassword, "kang", "kkkdh", "010-6666-5555");
        memberService.join(member);

        //when
        String jwt = memberService.login("test_id@kw.ac.kr", "test");

        //then
        assertThat(jwt).isEqualTo(""); // 로그인 실패한 경우 빈 토큰이 발급된다.
    }
}
