package com.example.tambang.service;

import com.example.tambang.domain.Member;
import com.example.tambang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = false)
    public String join(Member member){
        //memberRepository 계층을 이용해 member entity를 DB에 저장
        memberRepository.save(member);
        return member.getId();
    }
}
