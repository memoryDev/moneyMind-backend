package dev.momory.moneymindbackend.entity;

import dev.momory.moneymindbackend.dto.UpdateAccountOrCardRequest;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Account {

    public Account() {
    }

    public Account(AccountType accountType, String accountName, String accountNickname, BigDecimal balance) {
        this.accountType = accountType;
        this.accountName = accountName;
        this.accountNickname = accountNickname;
        this.balance = balance;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("계좌 고유 ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("사용자 고유 ID")
    User user;

    // transaction_id(FK) 작성필요함
    // 아직 애매 해서 작성하지는 않음..

    @Enumerated(EnumType.STRING)
    @Comment("계좌유형(은행계좌, 카드)")
    private AccountType accountType;

    @Comment("계좌 이름")
    private String accountName;

    @Comment("계좌 별명")
    private String accountNickname;

    @Comment("현재잔액")
    private BigDecimal balance;

    @Comment("계좌 생성 날짜")
    @CreatedDate
    private LocalDateTime createdAt;

    @Comment("계좌 수정 날짜")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * user 추가
     * @param user 조회된 유저정보
     */
    public void addUser(User user) {
        this.user = user;
    }

    /**
     * 게시글 업데이트
     */
    public void updateAccount(UpdateAccountOrCardRequest dto) {
        this.balance = dto.getBalance();
        this.accountName = dto.getAccountName();
        this.accountNickname = dto.getAccountNickname();
        this.accountType = dto.getAccountType();
    }

}
