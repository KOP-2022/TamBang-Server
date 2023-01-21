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

    public boolean checkMember(String id, String password){
        Member findMember = memberRepository.findOne(id);

        //id일치하는 회원 없거나, 비밀번호가 틀린 경우 로그인은 실패한다.
        if(findMember == null){
            return false;
        }
        else if(!findMember.getPassword().equals(password)){ //String 객체에 저장된 값을 비교해서 비밀번호 일치 여부를 판단
            return false;
        }
        return true;
    }
}
