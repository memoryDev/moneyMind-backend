package dev.momory.moneymindbackend.entity;

import dev.momory.moneymindbackend.dto.UpdateCategoryRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@ToString
public class Category {

    public Category() {
    }

    public Category(String name, String icon, CategoryFixedStatus fixedStatus) {
        this.name = name;
        this.icon = icon;
        this.fixedStatus = fixedStatus;
    }

    public Category(String name, String icon, CategoryFixedStatus fixedStatus, String userid) {
        this.name = name;
        this.icon = icon;
        this.fixedStatus = fixedStatus;
        this.userid = userid;
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

    @Column(nullable = true)
    @Comment("사용자 id")
    private String userid;

    /**
     * 카테고리 수정 할시
     */
    public void updateCateroies(UpdateCategoryRequest updateCategory) {
        this.name = updateCategory.getName();
        this.icon = updateCategory.getIcon();
    }
}
