package jpabook.jpashop.Controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    // 공통 속성
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // 책과 관련된 속성성
    private String author;
    private String isbn;
}
