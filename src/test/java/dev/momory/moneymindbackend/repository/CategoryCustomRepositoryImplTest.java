package dev.momory.moneymindbackend.repository;

import dev.momory.moneymindbackend.entity.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
class CategoryCustomRepositoryImplTest {

    @Autowired
    private CategoryCustomRepository repository;

    @Test
    @DisplayName("테스트")
    void testCategory() {

        List<Category> categories = repository.testCategory();

        Assertions.assertEquals(3, categories.size());

    }

}