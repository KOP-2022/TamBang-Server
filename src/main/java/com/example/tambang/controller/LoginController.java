package com.example.tambang.controller;

import com.example.tambang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @PostMapping("/login") //json을 반환하는 post request handler
    public Map<String, Object> login(@RequestBody LoginForm form){
        boolean isSuccess = memberService.checkMember(form.getId(), form.getPassword());

        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("success", isSuccess);

        return responseBody;
    }
}
