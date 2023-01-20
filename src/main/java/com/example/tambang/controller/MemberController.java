package com.example.tambang.controller;

import com.example.tambang.domain.Member;
import com.example.tambang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public Map<String, Object> createMember(@RequestBody MemberForm form){
        System.out.println("param: " + form.getId() + " " + form.getPassword());

        Member member = new Member();
        member.createMember(form.getId(), form.getPassword(), form.getName(), form.getNickname(), form.getPhoneNumber(), form.getEmail());

        String memberId = memberService.join(member);

        //반환할 정보를 hashmap으로 생성 {"success" : "true"}
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("success", true);
        returnData.put("member_id", memberId);

        return returnData;
    }
}
