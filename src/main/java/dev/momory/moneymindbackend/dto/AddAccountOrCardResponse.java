package dev.momory.moneymindbackend.dto;

import dev.momory.moneymindbackend.entity.Account;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AddAccountOrCardResponse {

    private Long id;

    public AddAccountOrCardResponse toDTO(Account account) {
       this.id = account.getId();
       return this;
    }
}
