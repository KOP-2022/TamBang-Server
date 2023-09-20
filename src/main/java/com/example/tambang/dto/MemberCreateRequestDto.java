package com.example.tambang.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberCreateRequestDto {
    private final String email;
    private final String password;
    private final String name;
    private final String nickname;
    private final String phoneNumber;
}
