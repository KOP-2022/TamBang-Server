package com.example.tambang.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class RealEstateFacility {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // facility와 real_estate 사이의 many to many 관계를
    // RealEstateFacility entity를 만들어
    // 다음과 같이 many to one 관계 2개를 설정하여 하는 것이 더 용이하다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;
}
