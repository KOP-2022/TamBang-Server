package com.example.tambang.domain;

import javax.persistence.*;

@Entity
public class RealEstateFacility {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;
}
