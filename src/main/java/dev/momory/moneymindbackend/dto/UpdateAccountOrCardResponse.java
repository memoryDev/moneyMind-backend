package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.Account;
import dev.momory.moneymindbackend.entity.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateAccountOrCardResponse {
    private Long id;                     // 고유번호
    private BigDecimal balance;          // 잔액
    private String accountName;          // 이름
    private String accountNickname;      // 별명
    private AccountType accountType;     // 타입
    private String userid;               // 작성자
    private LocalDateTime createdAt;     // 작성일
    private LocalDateTime updatedAt;     // 수정일

    /**
     * entity -> DTO
     */
    public UpdateAccountOrCardResponse toDTO(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
        this.accountName = account.getAccountName();
        this.accountNickname = account.getAccountNickname();
        this.accountType = account.getAccountType();
        this.userid = account.getUser().getUserid();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();

        return this;
    }
}
