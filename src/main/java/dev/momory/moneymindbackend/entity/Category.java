package dev.momory.moneymindbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
public class Category {

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
}
