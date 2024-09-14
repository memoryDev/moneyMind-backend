package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.dto.CategorySearchDTO;
import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<Page<Category>> getCategories(@Valid CategorySearchDTO searchDTO) {

        Page<Category> page = categoryService.getCategories(searchDTO);

        log.info("CategoryController.getCategories.searchDTO = {}", searchDTO);
        return ResponseEntity.ok(page);
    }


}
