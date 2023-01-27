package com.example.tambang.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long realEstateId; //객체 매핑으로 대체될 예정

    @Column(name = "reg_date_time")
    private LocalDateTime regDateTime;

    @Column(name = "update_date_time")
    private LocalDateTime updateDateTime;

    private String comment;
}
