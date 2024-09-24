package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.dto.CategoryDetailResponse;
import dev.momory.moneymindbackend.dto.CategorySearchDTO;
import dev.momory.moneymindbackend.dto.UpdateCategoryRequest;
import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.entity.CategoryFixedStatus;
import dev.momory.moneymindbackend.entity.User;
import dev.momory.moneymindbackend.exception.CustomException;
import dev.momory.moneymindbackend.repository.CategoryCustomRepository;
import dev.momory.moneymindbackend.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryCustomRepository categoryRepository;
    private final UserRepository userRepository;

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

    public CategoryDetailResponse getCategoryDetails(Integer id) {

        // 카테고리 조회
        Category category = categoryRepository.findById(id);

        // 조회된 카테고리가 없을경우
        if (ObjectUtils.isEmpty(category)) {
            throw new CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "조회된 카테고리 상세정보가 존재하지 않습니다. 확인해주세요.", "ERR_EMPTY_CATEGORY_DATA");
        }

        return new CategoryDetailResponse().toDTO(category);
    }

    public Boolean checkUseridDuplicate(String userid) {

        Boolean existsByUserid = userRepository.existsByUserid(userid);

        if (!existsByUserid) {
            log.warn("CategoryService.checkUseridDuplicate-userid - userid {} already exists", userid);
            throw new CustomException(HttpStatus.BAD_REQUEST, "사용자의 ID가 존재하지않습니다. 다시 로그인 해주세요.", "ERR_USER_NOT_FOUND");
        }

        return existsByUserid;
    }

    @Transactional
    public Category updateCategories(Integer id, String userid, UpdateCategoryRequest updateCategory) {

        // 1. 카테고리 조회
        Category categoryEntity = categoryRepository.findById(id);

        // 카테고리 게시글 조회X
        if (ObjectUtils.isEmpty(categoryEntity)) {
            throw new CustomException(HttpStatus.SERVICE_UNAVAILABLE, "게시글을 찾을수 없습니다. 다시 시도해 주세요.", "ERR_CATEGORY_NOT_FOUND");
        }

        // 2. 고정게시글은 수정 X
        if (categoryEntity.getFixedStatus() == CategoryFixedStatus.FIXED) {
            throw new CustomException(HttpStatus.FORBIDDEN, "고정된 게시글은 수정할 수 없습니다.", "ERR_CANNOT_DELETE_FIXED_POST");
        }

        // 3. 조회된 게시글과 토큰의 userid 비교
        if (!categoryEntity.getUserid().equals(userid)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "게시글을 수정할 권한이 없습니다. 작성자만 게시글 수정할 수 있습니다.", "ERR_NOT_AUTHOR");
        }

        // 4. 카테고리 중복체크
        Boolean existsName = categoryRepository.existsByNameAndIdNot(updateCategory.getName(), id);
        if (!existsName) {
            throw new CustomException(HttpStatus.CONFLICT, "카테고리 이름이 이미 존재합니다.", "CATEGORY_NAME_ALREADY_EXISTS");
        }

        // 4. 회원 수정
        categoryEntity.updateCateroies(updateCategory);

        return categoryEntity;
    }
}
