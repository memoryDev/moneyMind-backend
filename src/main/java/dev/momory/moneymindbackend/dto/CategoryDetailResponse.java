package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.entity.CategoryFixedStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDetailResponse {

    private Long id;
    private String name;
    private String icon;
    private CategoryFixedStatus fixedStatus;

    /**
     * entity -> DTO
     */
    public CategoryDetailResponse toDTO (Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.icon = category.getIcon();
        this.fixedStatus = category.getFixedStatus();

        return this;
    }
}
