package com.example.tambang.repository;

import com.example.tambang.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void save(Member member);
    Optional<Member> findOne(String email);
    List<Member> findAll();
}
