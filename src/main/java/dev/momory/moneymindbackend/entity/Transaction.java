package dev.momory.moneymindbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("거래 고유 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("사용자 고유 ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @Comment("카테고리 고유 ID")
    private Category category;

    @Comment("거래 금액")
    private BigDecimal amount;

    @Comment("거래 날짜")
    private LocalDateTime transactionDate;

    @Comment("거래 유형")
    private AccountTransactionType transactionType;

    @Comment("메모")
    @Column(columnDefinition = "TEXT")
    private String memo;

    @CreatedDate
    @Comment("거래 생성 날짜")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Comment("거래 수정 날짜")
    private LocalDateTime updatedAt;






}
