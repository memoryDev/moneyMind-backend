package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.entity.CategoryFixedStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDetailResponse {

    private Long id;
    private String name;
    private String icon;
    private CategoryFixedStatus fixedStatus;
    private String userid;
    private boolean isMyPost; // 내가 작성한글인지 여부 체크

    /**
     * entity -> DTO
     */
    public CategoryDetailResponse toDTO (Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.icon = category.getIcon();
        this.fixedStatus = category.getFixedStatus();
        this.userid = category.getUserid();

        return this;
    }

    /**
     * 내가작성글인지 체크
     */
    public void categoryIsMyPost(String tokenUserid) {
        System.out.println("tokenUserid = " + tokenUserid);
        System.out.println(this.userid);
        if (tokenUserid.equals(this.userid)) {
            this.isMyPost = true;
        } else {
            this.isMyPost = false;
        }
    }
}
