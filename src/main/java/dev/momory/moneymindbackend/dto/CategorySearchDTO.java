package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.SearchType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@ToString
public class CategorySearchDTO {

    private int page;
    private int size = 10;

    @NotNull(message = "검색 타입을 선택해 주세요.")
    private SearchType searchType;
    private String searchValue;
    private Pageable pageable;

    public CategorySearchDTO(int page, SearchType searchType, String searchValue) {
        this.page = page;
        this.searchType = searchType;
        this.searchValue = searchValue;
        this.pageable = PageRequest.of(page, size);
    }
}
