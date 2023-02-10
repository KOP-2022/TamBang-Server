package com.example.tambang.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@RequiredArgsConstructor
@Getter @Setter
public class MemberForm { //사실상 DTO(Data Transfer Object)와 같다.
    private String email;
    private String password;

    private String name;
    private String nickname;

    private String phoneNumber;
}
