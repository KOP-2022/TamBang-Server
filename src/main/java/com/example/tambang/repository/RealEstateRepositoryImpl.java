package com.example.tambang.repository;

import com.example.tambang.domain.Member;
import com.example.tambang.domain.RealEstate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RealEstateRepositoryImpl implements RealEstateRepository{

    private final EntityManager em;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //영속성 컨텍스트에 RealEstate 객체를 영속화
    public void save(RealEstate realEstate, String memberEmail){
        Member findMember = null;
        try {
            //JPQL을 이용한 조회
            findMember = em.createQuery("select m from Member m where email = :email", Member.class)
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

        logger.info("realEstate:{}",realEstate);
        //영속성 컨텍스트에 영속화
        em.persist(realEstate);
    }
    //영속성 컨텍스트에서 매물 찾기
    public RealEstate findOne(Long id){
        return em.find(RealEstate.class, id);
    }
}
