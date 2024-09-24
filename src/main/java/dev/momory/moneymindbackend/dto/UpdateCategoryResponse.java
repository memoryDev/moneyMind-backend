package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.entity.CategoryFixedStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateCategoryResponse {

    private Long id;
//    private String name;
//    private String icon;
//    private CategoryFixedStatus fixedStatus;
//    private String userid;

    /**
     * Entity->DTO
     */
    public UpdateCategoryResponse toDTO(Category category) {
        this.id = category.getId();
//        this.name = category.getName();
//        this.icon = category.getIcon();
//        this.fixedStatus = category.getFixedStatus();
//        this.userid = category.getUserid();
        
        return this;
    }
}
