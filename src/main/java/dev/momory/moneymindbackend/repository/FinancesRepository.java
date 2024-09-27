package dev.momory.moneymindbackend.repository;

import dev.momory.moneymindbackend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancesRepository extends JpaRepository<Account, Long>,  FinancesCustomRepository{

}
