package jpabook.jpashop.Repository;

import jpabook.jpashop.Domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { // Item은 처음에 JPA 저장하기 전까지 id가 없음. 즉, 새로 등록하는 객체.
            em.persist(item);
        } else { // DB로부터 가져온 경우
            em.merge(item); // update 비슷
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
