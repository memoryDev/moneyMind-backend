package dev.momory.moneymindbackend.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.entity.CategoryFixedStatus;
import dev.momory.moneymindbackend.validation.EnumValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
// 카테고리 추가
public class AddCategoryRequest {

    @NotBlank(message = "카테고리 아이콘 입력해주세요.")
    private String icon;

    @NotBlank(message = "카테고리 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "카테고리 고정 여부를 입력해주세요.")
    @EnumValid(target = CategoryFixedStatus.class, message = "카테고리 고정상태를 확인해주세요.")
    private CategoryFixedStatus fixedStatus;

    @JsonSetter("fixedStatus")
    public void setFixedStatus(String fixedStatus) {
        if (fixedStatus == null || fixedStatus.trim().isEmpty()) {
            this.fixedStatus = null;
        } else {
            this.fixedStatus = CategoryFixedStatus.valueOf(fixedStatus);
        }
    }

    /**
     * DTO -> ENTITY 변환
     */
    public Category toEntity() {
        return new Category(name, icon, fixedStatus);
    }

    /**
     * DTO -> ENTITY 변환
     * @param userid access토큰에서 추출한 userid
     * @return
     */
    public Category toEntity(String userid) {
        return new Category(name, icon, fixedStatus, userid);
    }
}
