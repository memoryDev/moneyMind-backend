package dev.momory.moneymindbackend.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("/api/test")
    public String test() {

        log.info("===== 통신 성공 하였습니다 =====");

        return "success";
    }
}
