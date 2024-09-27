package dev.momory.moneymindbackend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.momory.moneymindbackend.entity.Account;
import dev.momory.moneymindbackend.entity.QAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static dev.momory.moneymindbackend.entity.QAccount.account;

@Repository
@RequiredArgsConstructor
public class FinancesCustomRepositoryImpl implements FinancesCustomRepository {

    private final JPAQueryFactory queryFactory;
}
