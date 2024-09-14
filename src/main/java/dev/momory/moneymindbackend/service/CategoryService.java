package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.dto.CategorySearchDTO;
import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.repository.CategoryCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryCustomRepository categoryRepository;

    public Page<Category> getCategories(CategorySearchDTO searchDTO) {

        return categoryRepository.getCategories(searchDTO);
    }
}
