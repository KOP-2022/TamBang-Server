package com.example.tambang.configuration.security;

import com.example.tambang.domain.Member;
import com.example.tambang.repository.MemberRepository;
import com.example.tambang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> findMemberOpt = memberRepository.findOne(username);
        if(findMemberOpt.isEmpty()){
            throw new UsernameNotFoundException("회원을 찾지 못했습니다.");
        }
        // 회원 찾기에 성공한 경우
        UserDetailsImpl userDetails = new UserDetailsImpl(findMemberOpt.get());
        return userDetails;
    }
}
