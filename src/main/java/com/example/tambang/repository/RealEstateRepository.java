package com.example.tambang.repository;

import com.example.tambang.domain.RealEstate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RealEstateRepository {

    private final EntityManager em;

    //em에 매물 저장
    public void save(RealEstate realEstate){
        em.persist(realEstate);
    }
    //em에서 매물 찾기
    public RealEstate findOne(Long id){
        return em.find(RealEstate.class, id);
    }
    //member id를 통해 매물리스트 뽑는거?

    //일단 중복 매물 검증할 단서가 없음
}
