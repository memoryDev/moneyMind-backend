package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.entity.CategoryFixedStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddCategoryResponse {

    private Long id;
    private String name;
    private CategoryFixedStatus fixedStatus;
    private String icon;
    private String userid;

    /**
     * entity -> Response DTO
     */
    public AddCategoryResponse toDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.fixedStatus = category.getFixedStatus();
        this.icon = category.getIcon();
        this.userid = category.getUserid();

        return this;
    }
}
