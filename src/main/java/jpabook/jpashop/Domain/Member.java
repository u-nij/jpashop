package jpabook.jpashop.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    // @NotEmpty // Api V1
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // ORDERS 테이블에 있는 member에 의해 매핑됨
    private List<Order> orders = new ArrayList<>();

}
