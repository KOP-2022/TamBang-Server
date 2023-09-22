package com.example.tambang.dto;

import com.example.tambang.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberSearchResult {
    private final String email;
    private final String name;
    private final String nickname;
    private final String phoneNumber;

    public MemberSearchResult(Member member){
        this.email = member.getEmail();
        this.name = member.getName();;
        this.nickname = member.getNickname();
        this.phoneNumber = member.getPhoneNumber();
    }
}
