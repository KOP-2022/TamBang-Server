package com.example.tambang.service;

import com.example.tambang.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    String join(Member member);
    String login(String email, String passwd);
    Optional<Member> findByEmail(String email);
    List<Member> findAll();
}
