package com.example.tambang.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class LoginForm { //DTO for login

    private String email;
    private String password;
}
