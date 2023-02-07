package com.example.tambang.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
public class Member {

    @Id //직접 생성한 값을 기본키로 설정한다.
    private String id;
    private String password;

    private String name;
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;

    private String authority;

    @Column(name = "delete_yn")
    private String deleteYN;

    @Column(name = "suspension_yn")
    private String suspensionYN;

    //생성자 메서드로 사용하자.
    public void createMember(String id, String password, String name, String nickname, String phoneNumber, String email){
        this.id = id;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
