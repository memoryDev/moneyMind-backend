package dev.momory.moneymindbackend.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.momory.moneymindbackend.dto.CategorySearchDTO;
import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.entity.SearchType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dev.momory.moneymindbackend.entity.QCategory.category;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Category> testCategory() {
        return queryFactory
                .select(category)
                .from(category)
                .fetch();
    }

    @Override
    public Page<Category> getCategories(CategorySearchDTO searchDTO) {

        Pageable pageable = searchDTO.getPageable();

        List<Category> results = queryFactory.selectFrom(category)
                .where(
                        searchTypeEq(searchDTO.getSearchType(), searchDTO.getSearchValue())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(category)
                .where(
                        searchTypeEq(searchDTO.getSearchType(), searchDTO.getSearchValue())
                )
                .fetch().size();

        return new PageImpl<>(results, pageable, totalSize);
    }

    @Override
    public void addCategories(Category category) {
        em.persist(category);
    }

    @Override
    public Boolean existsByName(String name) {
        return queryFactory.selectFrom(category)
                .where(category.name.eq(name))
                .fetch().isEmpty();
    }

    @Override
    public Boolean existsByNameAndIdNot(String name, Integer id) {
        return queryFactory.selectFrom(category)
                .where(category.name.eq(name).and(category.id.ne(id.longValue())))
                .fetch().isEmpty();
    }

    @Override
    public Category findById(Integer id) {
        return em.find(Category.class, id);
    }

    private BooleanExpression searchTypeEq(SearchType searchType, String searchValue) {
        if (searchType == null || searchValue.isEmpty()) {
            return null;
        }

        switch (searchType) {
            case NAME:
                return category.name.containsIgnoreCase(searchValue);
            default :
                return null;
        }


    }

}
