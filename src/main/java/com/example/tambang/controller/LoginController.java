package com.example.tambang.controller;

import com.example.tambang.domain.Member;
import com.example.tambang.service.MemberService;
import com.example.tambang.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @PostMapping("/login") //json을 반환하는 post request handler
    public Map<String, Object> login(HttpServletRequest request, @RequestBody LoginForm form){
        Optional<Member> member = memberService.login(form.getEmail(), form.getPassword());
        Map<String, Object> responseBody = new HashMap<>();

        //로그인 요청을 보낸 고객 정보로 entity가 있는 경우
        if(member.isPresent()){
            //session이 없다면 생성, 있으면 session 반환
            HttpSession session = request.getSession();
            //session에 속성 setting
            session.setAttribute("loginMember", member.get());

            //로그인의 성공 여부를 작성해서 반환한다.
            responseBody.put("success", true);
        }
        //로그인 요청을 보낸 고객 정보로 member entity가 없는 경우
        else{
            responseBody.put("success", false);
        }

        return responseBody;
    }
}
