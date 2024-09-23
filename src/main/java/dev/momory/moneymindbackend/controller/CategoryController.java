package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.dto.AddCategoryRequest;
import dev.momory.moneymindbackend.dto.AddCategoryResponse;
import dev.momory.moneymindbackend.dto.CategoryDetailResponse;
import dev.momory.moneymindbackend.dto.CategorySearchDTO;
import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.exception.CustomException;
import dev.momory.moneymindbackend.response.ResponseDTO;
import dev.momory.moneymindbackend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 목록 조회
     * @param searchDTO
     * @return 카테고리 목록 및 페이징 정보
     */
    @GetMapping("/api/categories")
    public ResponseDTO<?> getCategories(@Valid CategorySearchDTO searchDTO) {

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize());
        searchDTO.setPageable(pageable);
        Page<Category> page = categoryService.getCategories(searchDTO);

        log.info("CategoryController.getCategories.searchDTO = {}", searchDTO);
        return ResponseDTO.successResponse(page, "success");
    }

    /**
     * 카테고리 추가
     * @param addCategory 카테고리 추가 정보
     * @return 카테고리 추가 성공 및 실패 메세지
     */
    @PostMapping("/api/categories")
    public ResponseDTO<?> addCategories(@Valid @RequestBody AddCategoryRequest addCategory) {

        // DTO NULL 일시
        if (addCategory == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다. 확인해주세요.", "ERR_NULL_ENTITY");
        }

        // dto -> entity 변환
        Category category = addCategory.toEntity();

        categoryService.addCategories(category);
        log.info("CategoryController.addCategories.addCategory = {}", category);

        // Entity -> DTO 변환
        AddCategoryResponse dto = new AddCategoryResponse().toDTO(category);

        return ResponseDTO.successResponse(dto, "카테고리 추가 성공하였습니다.");
    }

    /**
     * 카테고리 상세 조회
     * @param id 카테고리 게시글 번호
     * @return 카테고리 상세 조회 성공 및 실패 메세지
     */
    @GetMapping("/api/categories/{id}")
    public ResponseDTO<?> getCategoryDetails(@PathVariable Integer id) {

        // 입력받은 값이 null이거나, 0번이하이거나
        if (id == null || id <= 0) {
            log.warn("CategoryController.getCategoryDetails ERR_MISSING_PATH_PARAMETER = {}", id);
            throw new CustomException(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다. 확인해주세요.", "ERR_MISSING_PATH_PARAMETER");
        }

        CategoryDetailResponse categoryDTO = categoryService.getCategoryDetails(id);

        return ResponseDTO.successResponse(categoryDTO, "카테고리 상세 조회 success");
    }

    /**
     * 카테고리 상세정보 수정
     */


}
