package com.example.tambang.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
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
    public void createMember(String email, String password, String name, String nickname, String phoneNumber){
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
