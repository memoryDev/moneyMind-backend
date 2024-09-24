package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.dto.*;
import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.exception.CustomException;
import dev.momory.moneymindbackend.jwt.JWTUtil;
import dev.momory.moneymindbackend.response.ResponseDTO;
import dev.momory.moneymindbackend.service.AuthService;
import dev.momory.moneymindbackend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthService authService;
    private final JWTUtil jwtUtil;

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
    public ResponseDTO<?> addCategories(@Valid @RequestBody AddCategoryRequest addCategory
                                        ,@RequestHeader(name = "access") String accessToken) {
        // DTO NULL 일시
        if (addCategory == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다. 확인해주세요.", "ERR_NULL_ENTITY");
        }

        // access 체크
        if (!StringUtils.hasText(accessToken)) {
            log.warn("CategoryController.addCategories: ERR_TOKEN_EXPIRED");
            throw new CustomException(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다. 다시 로그인 해주세요.", "ERR_TOKEN_EXPIRED");
        }

        // access token 에서 userid 추출
        String userid = jwtUtil.getUserid(accessToken);

        // 사용자의 아이디가 존재하는지확인
        // 사용자의 아이디가 존재하지않을경우 종료됨
        categoryService.checkUseridDuplicate(userid);

//        // dto -> entity 변환
        Category category = addCategory.toEntity(userid);
        log.info("CategoryController.addCategories: category = {}", category);

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
    public ResponseDTO<?> getCategoryDetails(@PathVariable Integer id,
                                             @RequestHeader(name = "access") String accessToken) {

        // 입력받은 값이 null이거나, 0번이하이거나
        if (id == null || id <= 0) {
            log.warn("CategoryController.getCategoryDetails ERR_MISSING_PATH_PARAMETER = {}", id);
            throw new CustomException(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다. 확인해주세요.", "ERR_MISSING_PATH_PARAMETER");
        }

        // access 체크
        if (!StringUtils.hasText(accessToken)) {
            log.warn("CategoryController.getCategoryDetails: ERR_TOKEN_EXPIRED");
            throw new CustomException(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다. 다시 로그인 해주세요.", "ERR_TOKEN_EXPIRED");
        }

        // access token 에서 userid 추출
        String userid = jwtUtil.getUserid(accessToken);

        // 사용자의 아이디가 존재하는지확인
        // 사용자의 아이디가 존재하지않을경우 종료됨
        categoryService.checkUseridDuplicate(userid);

        CategoryDetailResponse categoryDTO = categoryService.getCategoryDetails(id);
        categoryDTO.categoryIsMyPost(userid);

        return ResponseDTO.successResponse(categoryDTO, "카테고리 상세 조회 success");
    }

    /**
     * 카테고리 상세정보 수정
     */
    @PutMapping("/api/categories/{id}")
    public ResponseDTO<?> updateCategories(@PathVariable Integer id
                                           , @RequestBody UpdateCategoryRequest categoryRequest
                                           , @RequestHeader(name = "access") String accessToken
                                           ) {

        // 입력받은 값이 null이거나, 0번이하이거나
        if (id == null || id <= 0) {
            log.warn("CategoryController.updateCategories ERR_MISSING_PATH_PARAMETER = {}", id);
            throw new CustomException(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다. 확인해주세요.", "ERR_MISSING_PATH_PARAMETER");
        }

        // access 체크
        if (!StringUtils.hasText(accessToken)) {
            log.warn("CategoryController.getCategoryDetails: ERR_TOKEN_EXPIRED");
            throw new CustomException(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다. 다시 로그인 해주세요.", "ERR_TOKEN_EXPIRED");
        }

        // access token 에서 userid 추출
        String userid = jwtUtil.getUserid(accessToken);

        // 사용자의 아이디가 존재하는지확인
        // 사용자의 아이디가 존재하지않을경우 종료됨
        categoryService.checkUseridDuplicate(userid);

        Category categoryEntity = categoryService.updateCategories(id, userid, categoryRequest);
        UpdateCategoryResponse responseDTO = new UpdateCategoryResponse().toDTO(categoryEntity);

        return ResponseDTO.successResponse(responseDTO, "게시글 수정 성공하였습니다.");
    }


}
