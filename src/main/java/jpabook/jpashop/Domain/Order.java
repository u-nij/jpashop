package jpabook.jpashop.Domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // 관례
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    // 연관관계 편의 메서드
    // Member
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItems(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // Delivery
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // == 생성 메서드 == //
    // 밖에서 Order에서 set을 통해 하는 값을 생성하는 방식이 아니라,
    // createOrder를 생성해 값을 넣게 함
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItems(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // ORDER로 강제
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // == 비즈니스 로직 == //
    // 주문 취소
    public void cancel() {
        // 상품이 배송 중일 경우
        if (delivery.getStatus() == DeliveryStatus.CDMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }
        
        // 상태 변경
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) { // 강조 혹은 이름이 똑같을 때를 제외하면 'this.'를 잘 쓰지 않음
            orderItem.cancel(); // order 1번 할 떄, item을 2개 주문하면 각각 cancel 처리해줘야 한다.
        }
    }

    // == 조회 로직 == //
    // 전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice(); // OrderItem에 주문 수량과 가격이 있기 때문에
        }
        return totalPrice;
        /* 한줄로 바꿀 수 있음
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
        */
    }
}