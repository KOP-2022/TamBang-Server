package com.example.tambang.jwt;

import com.example.tambang.configuration.jwt.JwtProvider;
import com.example.tambang.domain.Member;
import com.example.tambang.dto.MemberCreateRequestDto;
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

    private static final String TEST_EMAIL = "test@kw.ac.kr";
    private static final String TEST_PASSWORD = "test_passwd";
    private static final String TEST_NAME = "kang";
    private static final String TEST_NICK_NAME = "kkkdh";
    private static final String TEST_PHONE_NUMBER = "010-1111-1111";

    @Test
    public void 토큰_발급(){
        //given
        Member member = new Member();
        String testPassword = "test_passwd";
        String encodedPassword = passwordEncoder.encode(testPassword);

        String memberEmail = memberService.join(
                new MemberCreateRequestDto(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_NICK_NAME, TEST_PHONE_NUMBER)
        );

        // when
        String testToken = memberService.login(TEST_EMAIL, TEST_PASSWORD);
        System.out.println("testToken = " + testToken);

        // then (token에서 정보 추출)
        String memberEmailFromJwt = jwtProvider.getUserEmail(testToken);
        boolean expired = jwtProvider.isExpired(testToken);
        Authentication authentication = jwtProvider.getAuthentication(testToken);

        Assertions.assertThat(expired).isEqualTo(false);
        Assertions.assertThat(memberEmailFromJwt).isEqualTo(memberEmail);
    }
}
