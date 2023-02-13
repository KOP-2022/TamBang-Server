package com.example.tambang.repository;

import com.example.tambang.domain.Facility;
import com.example.tambang.domain.RealEstateFacility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class FacilityRepositoryImpl implements FacilityRepository {

    private final EntityManager em;

    @Override
    public void enrollWithRealEstate(Facility facility, RealEstateFacility realEstateFacility){
        em.persist(facility);
        em.persist(realEstateFacility);
    }
    //이미 존재하는 편의시설 정보인지 확인한다. (kakao local api의 id 필드를 기준으로 찾는다.)
    @Override
    public boolean check(String id) {
        try {
            Object result = em.createQuery("select f from Facility f where kakaoId =: id")
                    .setParameter("id", id)
                    .getSingleResult();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
