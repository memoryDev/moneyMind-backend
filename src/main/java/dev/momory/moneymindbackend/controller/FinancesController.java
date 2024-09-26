package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.service.FinancesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FinancesController {

    private final FinancesService financesService;
}
