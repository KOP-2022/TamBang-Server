package com.example.tambang.service;

import com.example.tambang.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    @DisplayName("회원등록")
    public void 회원등록() throws Exception {
        System.out.println("test start!!");
        Member member = new Member();
        member.createMember("test_id", "test_passwd", "kang", "kkkdh", "010-6666-5555", "test@kw.ac.kr");

        String memberId = memberService.join(member);
        assertThat(memberId).isEqualTo("test_id");
    }

    @Test(expected = IllegalStateException.class)
    @DisplayName("중복된 아이디의 회원 등록")
    public void 중복_회원_등록() throws Exception {
        Member member1 = new Member();
        Member member2 = new Member();

        member1.createMember("test_id", "test_passwd", "kang", "kkkdh", "010-6666-5555", "test@kw.ac.kr");
        member2.createMember("test_id", "test_passwd", "kong", "kkkdh1", "010-6666-5555", "test@kw.ac.kr");

        memberService.join(member1);
        memberService.join(member2);

        Assert.fail("예외가 발생해야 한다.");
    }

    @Test
    @DisplayName("로그인 테스트")
    public void 로그인() throws Exception{
        Member member = new Member();
        member.createMember("test_id", "test_passwd", "kang", "kkkdh", "010-6666-5555", "test@kw.ac.kr");

        memberService.join(member);

        boolean isSuccess = memberService.checkMember("test_id", "test_passwd");

        assertThat(isSuccess).isEqualTo(true);
    }
}
