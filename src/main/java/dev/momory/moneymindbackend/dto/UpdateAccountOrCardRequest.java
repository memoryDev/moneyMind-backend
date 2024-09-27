package dev.momory.moneymindbackend.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import dev.momory.moneymindbackend.entity.AccountType;
import dev.momory.moneymindbackend.validation.EnumValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateAccountOrCardRequest {
    @NotNull(message = "잔액을 입력해주세요.")
    private BigDecimal balance;

    @NotBlank(message = "계좌 및 카드 이름을 입력해주세요.")
    private String accountName;

    @NotBlank(message = "계좌 및 카드 별명을 입력해주세요.")
    private String accountNickname;

    @NotNull(message = "타입을 입력해주세요.")
    @EnumValid(target = AccountType.class, message = "타입 입력해주세요.")
    private AccountType accountType;

    @JsonSetter("accountType")
    public void setAccountType(String accountType) {
        if (accountType == null || accountType.trim().isEmpty()) {
            this.accountType = null;
        } else {
            this.accountType = AccountType.valueOf(accountType);
        }
    }
}
