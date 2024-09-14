package dev.momory.moneymindbackend.repository;

import dev.momory.moneymindbackend.dto.CategorySearchDTO;
import dev.momory.moneymindbackend.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryCustomRepository {

    List<Category> testCategory();

    Page<Category>getCategories(CategorySearchDTO searchDTO);
}
