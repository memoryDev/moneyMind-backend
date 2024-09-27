package dev.momory.moneymindbackend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FinancesType {

    BANK("bank"),
    CARD("card");

    private final String value;
}
