package com.example.tambang.repository;

import com.example.tambang.domain.Facility;
import com.example.tambang.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class FacilityRepository {

    private final EntityManager em;

    public void enroll(Facility facility){
        em.persist(facility);
    }
}
