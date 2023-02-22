package com.example.tambang.repository;

import com.example.tambang.domain.Facility;
import com.example.tambang.domain.Member;
import com.example.tambang.domain.RealEstate;
import com.example.tambang.domain.RealEstateFacility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RealEstateRepositoryImpl implements RealEstateRepository{

    private final EntityManager em;

    //영속성 컨텍스트에 RealEstate 객체를 영속화
    @Override
    public void save(RealEstate realEstate, String memberEmail){
        Member findMember = null;
        try {
            //JPQL을 이용한 조회
            findMember = em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", memberEmail)
                    .getSingleResult();
//            System.out.println("findMember.toString() = " + findMember.toString());
        }
        catch(Exception e){
            e.printStackTrace();
            return;
        }
        //등록된 매물에 집주인과 연관관계를 맺어준다.
        realEstate.setOwner(findMember);
        //영속성 컨텍스트에 영속화
        em.persist(realEstate);
    }
    //영속성 컨텍스트에서 매물 찾기
    @Override
    public RealEstate findOne(Long id){
        return em.find(RealEstate.class, id);
    }

    @Override
    public List<Facility> findAroundFacilities(RealEstate realEstate){
        //realEstate를 이용해 RealEstateFacility entity list 찾은 다음, 객체 그래프 탐색을 이용해 편의시설 List collection을 만들면 된다.
        List<RealEstateFacility> realEstateFacilities = em.createQuery("select re from RealEstate re where re.realEstateId = :id", RealEstateFacility.class)
                .setParameter("id", realEstate.getId())
                .getResultList();
        
        List<Facility> facilities = new ArrayList<>();

        //RealEstateFacility entity를 이용해 Facility entity를 조회한다.
        for (RealEstateFacility realEstateFacility : realEstateFacilities) {
            facilities.add(realEstateFacility.getFacility());
        }
        
        //편의 시설 리스트 컬랙션을 반환
        return facilities;
    }
}
