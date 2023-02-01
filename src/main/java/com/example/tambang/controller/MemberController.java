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

        //반환할 정보를 hashmap으로 생성 {"success" : "true"}
        Map<String, Object> returnData = new HashMap<>();

        try{
            //중복된 아이디로 가입하는 경우 IllegalStateException 발생
            String memberId = memberService.join(member);
        }
        catch(IllegalStateException e){
            String msg = "회원 가입에 실패했습니다.";
            returnData.put("success", false);
            returnData.put("message", msg);

            return returnData;
        }
        //성공한 경우 "success" : "true" 반환하도록 한다.
        returnData.put("success", true);

        return returnData;
    }
}
