package dev.momory.moneymindbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class Category {

    public Category() {
    }

    public Category(String name, String icon, CategoryFixedStatus fixedStatus) {
        this.name = name;
        this.icon = icon;
        this.fixedStatus = fixedStatus;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("카테고리 고유 ID")
    private Long id;

    @Column(nullable = false)
    @Comment("카테고리 이름")
    private String name;

    @Column(nullable = false)
    @Comment("카테고리 아이콘")
    private String icon;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("카테고리 고정 여부")
    private CategoryFixedStatus fixedStatus;
}
