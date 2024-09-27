package dev.momory.moneymindbackend.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import dev.momory.moneymindbackend.entity.Account;
import dev.momory.moneymindbackend.entity.AccountType;
import dev.momory.moneymindbackend.entity.User;
import dev.momory.moneymindbackend.validation.EnumValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class AddAccountOrCardRequest {

    @NotNull(message = "타입을 입력해주세요.")
    @EnumValid(target = AccountType.class, message = "타입 입력해주세요.")
    private AccountType accountType; // 타입(통장, 카드)

    @NotNull(message = "잔액을 입력해주세요.")
    private BigDecimal balance; //현재잔액

    private String userid; // 사용자ID

    @NotBlank(message = "계좌 및 카드 이름을 입력해주세요.")
    private String accountName; //계좌or카드 이름

    @NotBlank(message = "계좌 및 카드 별명을 입력해주세요.")
    private String accountNickname; // 계좌or카드 별명

    @JsonSetter("accountType")
    public void setAccountType(String accountType) {
        if (accountType == null || accountType.trim().isEmpty()) {
            this.accountType = null;
        } else {
            this.accountType = AccountType.valueOf(accountType);
        }
    }

    /**
     * DTO -> ENTITY
     * @return
     */
    public Account toEntity() {
        return new Account(this.accountType, this.accountName, this.accountNickname, this.balance);
    }
}
