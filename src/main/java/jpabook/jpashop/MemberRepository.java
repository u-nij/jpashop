package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext // JPA를 쓰기 때문에, EntityManager 필요
    private EntityManager em;
    // Spring Container 위에서 동작. EntityManager가 자동 주입됨.

    // 저장
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
        // Id를 분리하는 이유: command와 query 분리
        // 저장 후 거의 return 값을 만들지 않지만, Id 정도만 조회하는 정도로 설계
    }

    // 조회
    public Member find(Long id) {
        return em.find(Member.class, id);
    }

}
