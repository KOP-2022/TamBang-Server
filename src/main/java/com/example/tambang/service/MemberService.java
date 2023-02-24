package com.example.tambang.service;

import com.example.tambang.domain.Member;

import java.util.Optional;

public interface MemberService {
    String join(Member member);
    Optional<Member> login(String email, String passwd);
    Optional<Member> findByEmail(String email);
}
