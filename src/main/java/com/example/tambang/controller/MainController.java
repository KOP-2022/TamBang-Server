package com.example.tambang.controller;

import com.example.tambang.domain.Member;
import com.example.tambang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    //로그인 서비스
    @PostMapping("/login") //json을 반환하는 post request handler
    public Map<String, Object> login(HttpServletRequest request, @RequestBody Form.LoginForm form){
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
    @ResponseBody
    @PostMapping("/logout")
    public Map<String, Object> login(HttpServletRequest request){
        //session 있는지 확인
        HttpSession session = request.getSession(false);
        Map<String, Object> responseBody = new HashMap<>();

        if(session != null){
            session.invalidate(); //session 무효화
            responseBody.put("success", true);
            return responseBody;
        }
        responseBody.put("success", false);
        return responseBody;
    }

    //회원 서비스
    @ResponseBody
    @PostMapping("/members")
    public Map<String, Object> createMember(@RequestBody Form.MemberForm form){
        System.out.println("param: " + form.getEmail() + " " + form.getPassword());

        Member member = new Member();
        member.createMember(form.getEmail(), form.getPassword(), form.getName(), form.getNickname(), form.getPhoneNumber());

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

    //회원 정보 조회
    @GetMapping("/members/{member-email}")
    public ResponseVO.MemberResponse getMember(@PathVariable("member-email") String memberEmail){
        ResponseVO.MemberResponse responseBody = null;

        Optional<Member> findMemberOpt = memberService.findByEmail(memberEmail);
        //회원 조회 성공한 경우 회원 정보와 함께 반환
        if(findMemberOpt.isPresent()){
            Member findMember = findMemberOpt.get();
            responseBody = new ResponseVO.MemberResponse(
                true, findMember.getEmail(), findMember.getName(), findMember.getNickname(), findMember.getPhoneNumber()
            );

            return responseBody;
        }
        //회원 조회에 실패한 경우 실패 여부만 반환
        responseBody = new ResponseVO.MemberResponse(false);

        return responseBody;
    }
}
