package com.example.tambang.repository;

import com.example.tambang.domain.Member;
import com.example.tambang.domain.RealEstate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RealEstateRepository {

    private final EntityManager em;

    //em에 매물 저장
    public void save(RealEstate realEstate, String member_id){
        Member findMember = em.find(Member.class, member_id);
        //등록된 매물에 집주인과 연관관계를 맺어준다.
        realEstate.setOwner(findMember);
        //영속성 컨텍스트에 영속화
        em.persist(realEstate);
    }
    //em에서 매물 찾기
    public RealEstate findOne(Long id){
        return em.find(RealEstate.class, id);
    }
    //member id를 통해 매물리스트 뽑는거?

    //일단 중복 매물 검증할 단서가 없음
}
