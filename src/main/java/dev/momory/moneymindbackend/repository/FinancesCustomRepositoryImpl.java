package dev.momory.moneymindbackend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.momory.moneymindbackend.dto.GetAccountOrCardListResponse;
import dev.momory.moneymindbackend.entity.Account;
import dev.momory.moneymindbackend.entity.QAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static dev.momory.moneymindbackend.entity.QAccount.account;

@Repository
@RequiredArgsConstructor
public class FinancesCustomRepositoryImpl implements FinancesCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetAccountOrCardListResponse> getAccountOrCardList(Pageable pageable, String userid) {

        List<GetAccountOrCardListResponse> results = queryFactory
                .select(Projections.bean(GetAccountOrCardListResponse.class,
                            account.id,
                            account.balance,
                            account.accountName,
                            account.accountNickname,
                            account.accountType,
                            account.user.userid,
                            account.createdAt,
                            account.updatedAt))
                .from(account)
                .where(account.user.userid.eq(userid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(account.id.desc())
                .fetch();

        int totalSize = queryFactory.selectFrom(account)
                .where(account.user.userid.eq(userid))
                .fetch().size();

        return new PageImpl<>(results, pageable, totalSize);
    }
}
