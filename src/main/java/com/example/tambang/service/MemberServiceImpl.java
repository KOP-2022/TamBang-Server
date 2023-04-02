package com.example.tambang.service;

import com.example.tambang.configuration.jwt.JwtProvider;
import com.example.tambang.configuration.security.UserDetailsImpl;
import com.example.tambang.domain.Member;
import com.example.tambang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 암호화된 비밀번호 인증을 위해 DI
    private final JwtProvider jwtProvider; // 토큰 util 관리
    @Transactional(readOnly = false)
    @Override
    public String join(Member member) throws IllegalStateException{
        try {
            validateDuplicateMember(member);
        }
        catch(IllegalStateException e){
            e.printStackTrace();
            return "";
        }
        // 비밀번호 암호화 (BCrypt algorithm 사용)
        member.setEncodedPasswd(passwordEncoder.encode(member.getPassword()));
        //memberRepository 계층을 이용해 member entity를 DB에 저장
        memberRepository.save(member);
        return member.getEmail();
    }

    //중복 아이디를 검사하는 method
    private void validateDuplicateMember(Member member) throws IllegalStateException{
        Optional<Member> findMember = memberRepository.findOne(member.getEmail());

        //Optional object가 비어있는지 확인한다.
        if(findMember.isPresent()){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    @Override
    public String login(String email, String password){
        Optional<Member> findMember = memberRepository.findOne(email);
        if(findMember.isEmpty()){
            return "";
        }
        System.out.println("login service email = " + email);
        //비밀번호까지 service component에서 비교해서 일치하는 경우 Optional 객체를 반환한다.
        String encodedPassword = findMember.get().getPassword();
//        System.out.println("password = " + password);
//        System.out.println("encodedPassword = " + encodedPassword);
        boolean isMatch = passwordEncoder.matches(password, encodedPassword);

        if(!isMatch){
            return "";
        }
        UserDetailsImpl userDetails = new UserDetailsImpl(findMember.get());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );

        String jwt = createJwt(authentication);
//        System.out.println("newly created jwt with login = " + jwt);
        return jwt;
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
