package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GetAccountOrCardListResponse {
    private Long id;                     // 고유번호
    private BigDecimal balance;          // 잔액
    private String accountName;          // 이름
    private String accountNickname;      // 별명
    private AccountType accountType;     // 타입
    private String userid;               // 작성자
    private LocalDateTime createdAt;     // 작성일
    private LocalDateTime updatedAt;     // 수정일
}




