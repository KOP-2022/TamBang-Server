package com.example.tambang.jwt;

import com.example.tambang.configuration.jwt.JwtProvider;
import com.example.tambang.domain.Member;
import com.example.tambang.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    public void 토큰_발급(){
        //given
        Member member = new Member();
        String testPassword = "test_passwd";
        String encodedPassword = passwordEncoder.encode(testPassword);
        member.createMember("test_id@kw.ac.kr", testPassword, "kang", "kkkdh", "010-6666-5555");
        member.grantAuthority("USER");
        memberService.join(member);

        // when
        String testToken = memberService.login("test_id@kw.ac.kr", "test_passwd");
        System.out.println("testToken = " + testToken);

        // then
        String userEmail = jwtProvider.getUserEmail(testToken);
        boolean expired = jwtProvider.isExpired(testToken);
        Authentication authentication = jwtProvider.getAuthentication(testToken);
        Assertions.assertThat(expired).isEqualTo(false);
        System.out.println("userEmail = " + userEmail);
    }
}
