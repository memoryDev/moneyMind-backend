package dev.momory.moneymindbackend.repository;

import dev.momory.moneymindbackend.dto.GetAccountOrCardListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FinancesCustomRepository {

    Page<GetAccountOrCardListResponse> getAccountOrCardList(Pageable pageable, String userid);
}
