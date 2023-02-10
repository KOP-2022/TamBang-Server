package com.example.tambang.service;

import com.example.tambang.domain.Member;

import java.util.Optional;

public interface MemberService {
    public String join(Member member);
    public Optional<Member> login(String email, String passwd);
}
