package com.example.tambang.repository;

import com.example.tambang.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{

    //EntityManager를 생성자 주입으로 의존관계를 주입 받을 것이다.
    private final EntityManager em;

    @Override
    public List<Member> findAll(){
        //JPQL을 이용해 전체 Member entity 객체 리스트를 반환
        List resultList = em.createQuery("select m from Member as m")
                .getResultList();
        
        return resultList;
    }
    @Override
    public Optional<Member> findOne(String email){
        List<Member> members = em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList();
        System.out.println("members.size() = " + members.size());
        return members.stream().findAny();
    }

    @Override
    public void save(Member member){
        //EntityManager를 이용해 영속성 컨텍스트에 member entity 객체를 보관
        em.persist(member);
    }
}
