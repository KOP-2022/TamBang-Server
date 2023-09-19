package com.example.tambang.service;

import com.example.tambang.configuration.jwt.JwtProvider;
import com.example.tambang.configuration.security.UserDetailsImpl;
import com.example.tambang.domain.Member;
import com.example.tambang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 암호화된 비밀번호 인증을 위해 DI
    private final JwtProvider jwtProvider; // 토큰 util 관리
    @Transactional
    @Override
    public String join(Member member) {
        validateDuplicateMember(member);
        // 비밀번호 암호화 (BCrypt algorithm 사용)
        member.setEncodedPasswd(passwordEncoder.encode(member.getPassword()));

        memberRepository.save(member);
        return member.getEmail();
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMember = memberRepository.findOne(member.getEmail());

        if(findMember.isPresent()){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    @Override
    public String login(String email, String password) {
        Optional<Member> findMember = memberRepository.findOne(email);
        if(findMember.isEmpty()){
            return "";
        }
        System.out.println("login service email = " + email);
        // 비밀번호까지 service component에서 비교해서 일치하는 경우 Optional 객체를 반환
        String encodedPassword = findMember.get().getPassword();
        boolean isMatch = passwordEncoder.matches(password, encodedPassword);

        if(!isMatch){
            return "";
        }
        UserDetailsImpl userDetails = new UserDetailsImpl(findMember.get());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );

        String jwt = createJwt(authentication);
        return jwt;
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    private String createJwt(Authentication authentication){
        Long expiredMs = 1000 * 60 * 60l; // 1시간의 유효 기간
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return jwtProvider.createToken(principal, expiredMs);
    }

    @Override
    public Optional<Member> findByEmail(String email){
        Optional<Member> member = memberRepository.findOne(email);
        return member;
    }
}
