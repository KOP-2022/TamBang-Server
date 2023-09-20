package com.example.tambang.domain;

import com.example.tambang.dto.MemberCreateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)//직접 생성한 값을 기본키로 설정한다.
    private Long id;
    private String email;
    private String password;

    private String name;
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String authority;

    @Column(name = "delete_yn")
    private String deleteYN;

    @Column(name = "suspension_yn")
    private String suspensionYN;

    //생성자 메서드로 사용하자.
    public static Member createMember(MemberCreateRequestDto requestDto){
        return new Member(requestDto.getEmail(),
                requestDto.getPassword(),
                requestDto.getName(),
                requestDto.getNickname(),
                requestDto.getPhoneNumber());
    }

    private Member(String email, String password, String name, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public void grantAuthority(String authority){
        this.authority = authority;
    }

    // 비밀번호 암호화한 형태로 변경
    public void setEncodedPasswd(String encodedPasswd){
        this.password = encodedPasswd;
    }
}
