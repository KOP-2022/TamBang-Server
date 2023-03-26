package com.example.tambang.controller;

import com.example.tambang.domain.Member;
import com.example.tambang.domain.RealEstate;
import com.example.tambang.service.MemberService;
import com.example.tambang.service.RealEstateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final RealEstateService realEstateService;
    //로그인 서비스
    @PostMapping("/login") //json을 반환하는 post request handler
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Form.LoginForm form){
        String jwtCreatedByLogin = memberService.login(form.getEmail(), form.getPassword());
        Map<String, Object> responseBody = new HashMap<>();

        // cookie로 jwt를 담아 전송한다.
//        System.out.println("jwtCreatedByLogin = " + jwtCreatedByLogin);
//        Cookie jwt = new Cookie("jwt", jwtCreatedByLogin);
//        jwt.setPath("/");       // 모든 path에서의 쿠키에 대한 접근을 허용한다.
//        jwt.setHttpOnly(false); // browser에서 cookie로의 접근을 허용
//
//        response.addCookie(jwt);

        addCookie(response, "jwt", jwtCreatedByLogin, 3600);

        //로그인 요청을 보낸 고객 정보로 jwt 생성에 성공한 경우
        if(jwtCreatedByLogin != ""){
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
    public Map<String, Object> logout(HttpServletRequest request){
        Map<String, Object> responseBody = new HashMap<>();
        // refresh token 구현이 없다면, 로그아웃이 의미가 없다.
        // token 유효 기간을 1시간으로 잡아서 짧게 쓰는 방식을 선택한다. 일단
        responseBody.put("success", false);
        return responseBody;
    }

    //회원 서비스
    @ResponseBody
    @PostMapping("/members")
    public ResponseVO.LoginResponse createMember(@RequestBody Form.MemberForm form){
        ResponseVO.LoginResponse responseBody = new ResponseVO.LoginResponse();
//        System.out.println("param: " + form.getEmail() + " " + form.getPassword());
        Member member = new Member();
        member.createMember(form.getEmail(), form.getPassword(), form.getName(), form.getNickname(), form.getPhoneNumber());
        // 일단 기본적으로 USER 권한 부여
        member.grantAuthority("USER");
        
        System.out.println("member = " + member);
        try{
            //중복된 아이디로 가입하는 경우 IllegalStateException 발생
            String memberId = memberService.join(member);
        }
        catch(IllegalStateException e){
            //Exception 발생 시에는 로그인 실패 결과를 전달
            String msg = "회원 가입에 실패했습니다.";

            responseBody.setSuccess(false);
            responseBody.getData().put("message", msg);

            return responseBody;
        }
        //성공한 경우 멤버 이메일 정보와 함께 로그인 성공을 알린다.
        responseBody.setSuccess(true);
        responseBody.getData().put("email", member.getEmail());

        return responseBody;
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

    @GetMapping("/map")
    public ResponseVO.RealEstateListResponse getRealEstateListInRadius(
            @RequestParam(required = true) Double latitude,
            @RequestParam(required = true) Double longitude,
            @RequestParam(required = true) Double radius){
        // 기준 좌표 중심으로 radius 거리 내의 모든 매물을 조회
        List<ResponseVO.RealEstateVO> aroundRealEstates = realEstateService.getAroundRealEstates(latitude, longitude, radius);
        // response body를 만든다.
        ResponseVO.RealEstateListResponse responseBody = new ResponseVO.RealEstateListResponse(true, aroundRealEstates);

        return responseBody;
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(true)
                .maxAge(maxAge)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
