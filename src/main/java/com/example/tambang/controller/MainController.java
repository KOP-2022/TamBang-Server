package com.example.tambang.controller;

import com.example.tambang.domain.Member;
import com.example.tambang.dto.*;
import com.example.tambang.service.MemberService;
import com.example.tambang.service.RealEstateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
//@RequestMapping("/api/v1")
@RestController
public class MainController {

    private final MemberService memberService;
    private final RealEstateService realEstateService;

    @PostMapping("/login") //json을 반환하는 post request handler
    public Map<String, Object> login(HttpServletResponse response, @RequestBody Form.LoginForm form){
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
        boolean isSuccess = !jwtCreatedByLogin.isEmpty();
        responseBody.put("success", isSuccess);

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
    public CommonResponseDto<MemberCreateResult> createMember(@RequestBody MemberCreateRequestDto requestDto){
        String memberEmail = memberService.join(requestDto);

        MemberCreateResult result = new MemberCreateResult(memberEmail);

        return CommonResponseDto.<MemberCreateResult>builder()
                .success(true)
                .data(result)
                .build();
    }

    //회원 정보 조회
    @GetMapping("/members/{member-email}")
    public CommonResponseDto<MemberSearchResult> getMember(@PathVariable("member-email") String memberEmail){
        MemberSearchResult result = memberService.findByEmail(memberEmail);
        //회원 조회 성공한 경우 회원 정보와 함께 반환
        return CommonResponseDto.<MemberSearchResult>builder()
                .data(result)
                .success(true)
                .build();
    }

    @GetMapping("/map")
    @Operation(description = "기준 좌표 중심으로 radius 거리 내의 모든 부동산 조회")
    public CommonResponseDto<RealEstateResults> getRealEstateListInRadius(RealEstateCoordinateRequestDto requestDto){
        List<RealEstateResult> aroundRealEstateList = realEstateService
                .getAroundRealEstates(requestDto.getLatitude(), requestDto.getLongitude(), requestDto.getRadius());
        RealEstateResults resultList = new RealEstateResults(aroundRealEstateList, aroundRealEstateList.size());

        return CommonResponseDto.<RealEstateResults>builder()
                .data(resultList)
                .success(true)
                .build();
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("None")
                .httpOnly(false)
                .secure(true)
                .maxAge(maxAge)
                .domain(".tambang.kro.kr")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
