package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.response.ResponseDTO;
import dev.momory.moneymindbackend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/api/transaction")
    public ResponseDTO<?> addTransaction () {

        return null;
    }
}
