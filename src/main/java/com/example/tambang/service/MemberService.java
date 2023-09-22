package com.example.tambang.service;

import com.example.tambang.domain.Member;
import com.example.tambang.dto.MemberCreateRequestDto;
import com.example.tambang.dto.MemberSearchResult;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    String join(MemberCreateRequestDto requestDto);
    String login(String email, String passwd);
    MemberSearchResult findByEmail(String email);
    List<Member> findAll();
}
