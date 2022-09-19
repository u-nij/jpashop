package jpabook.jpashop.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.Domain.Item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int OrderPrice; // 주문 가격
    private int count; // 주문 수량

    // == 생성 메서드 == //
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // item의 재고를 감소시킴
        return orderItem;
    }

    // == 비즈니스 로직 == //
    public void cancel() { // OrderItem에서는 cancel()를 통해
        getItem().addStock(count); // item의 재고를 주문했던 수량만큼 늘려줘야 한다
    }

    // == 조회 로직 == //
    // 주문 상품 전제 가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
