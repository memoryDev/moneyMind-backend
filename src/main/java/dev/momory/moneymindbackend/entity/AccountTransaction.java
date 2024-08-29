package dev.momory.moneymindbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("계좌 거래 고유ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @Comment("계좌 고유 ID")
    private Account account;;

    @Comment("거래 금액")
    private BigDecimal amount;

    @Comment("거래 날짜")
    private LocalDateTime transactionDate;

    @Comment("거래 유형")
    @Enumerated(EnumType.STRING)
    private AccountTransactionType type;

    @CreatedDate
    @Comment("거래 생성 날짜")
    private LocalDateTime createdAt;
}
