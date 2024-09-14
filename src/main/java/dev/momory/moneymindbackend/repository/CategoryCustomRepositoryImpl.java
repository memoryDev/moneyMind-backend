package dev.momory.moneymindbackend.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.momory.moneymindbackend.dto.CategorySearchDTO;
import dev.momory.moneymindbackend.entity.Category;
import dev.momory.moneymindbackend.entity.QCategory;
import dev.momory.moneymindbackend.entity.SearchType;
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

//        queryFactory
//                .selectFrom(category)
//                .where(
//                        searchTypeEq(searchDTO.getSearchType(), searchDTO.getSearchValue())
//                )
//                .fetch()

        return new PageImpl<>(results, pageable, results.size());
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
