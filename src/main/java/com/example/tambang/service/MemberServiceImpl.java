package com.example.tambang.service;

import com.example.tambang.domain.Member;
import com.example.tambang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = false)
    @Override
    public String join(Member member) throws IllegalStateException{
        validateDuplicateMember(member);
        //memberRepository 계층을 이용해 member entity를 DB에 저장
        memberRepository.save(member);
        return member.getEmail();
    }

    //중복 아이디를 검사하는 method
    private void validateDuplicateMember(Member member){
        Optional<Member> findMember = memberRepository.findOne(member.getEmail());

        //Optional object가 비어있는지 확인한다.
        if(findMember.isPresent()){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    @Override
    public Optional<Member> login(String email, String password){
        Optional<Member> findMember = memberRepository.findOne(email);

        if(findMember.isEmpty()){
            return Optional.empty();
        }

        //비밀번호까지 service component에서 비교해서 일치하는 경우 Optional 객체를 반환한다.
        if(findMember.get().getPassword().equals(password)){
            return findMember;
        }
        //일치하지 않는 경우 empty optional object를 만들어서 반환한다.
        else{
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email){
        Optional<Member> member = memberRepository.findOne(email);
        return member;
    }
}
