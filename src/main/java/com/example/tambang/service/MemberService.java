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
    public String join(Member member) throws IllegalStateException{
        validateDuplicateMember(member);
        //memberRepository 계층을 이용해 member entity를 DB에 저장
        memberRepository.save(member);
        return member.getId();
    }

    //중복 아이디를 검사하는 method
    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findOne(member.getId());

        if(findMember != null){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }
}
