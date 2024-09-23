package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.dto.CategorySearchDTO;
import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.exception.CustomException;
import dev.momory.moneymindbackend.repository.CategoryCustomRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryCustomRepository categoryRepository;

    public Page<Category> getCategories(CategorySearchDTO searchDTO) {
        return categoryRepository.getCategories(searchDTO);
    }

    @Transactional
    public void addCategories(Category category) {

        // category - name 유효성 체크
        if (StringUtils.isBlank(category.getName())) {
            log.warn("CategoryService.addCategories - category name required");
            throw new CustomException(HttpStatus.BAD_REQUEST, "카테고리 이름 입력해주세요.", "CATEGORY_NAME_REQUIRED");
        }

        // 이미 저장된 name 값 인지 체크
        // 존재-> false, 미존재 -> true
        Boolean existsName = categoryRepository.existsByName(category.getName());
        if (!existsName) {
            log.warn("CategoryService.addCategories - {} - category name already exists", category.getName());
            throw new CustomException(HttpStatus.CONFLICT, "카테고리 이름이 이미 존재합니다.", "CATEGORY_NAME_ALREADY_EXISTS");
        }

        // 카테고리 추가
        categoryRepository.addCategories(category);
    }
}
