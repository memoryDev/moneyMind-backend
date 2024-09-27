package dev.momory.moneymindbackend.controller;

import dev.momory.moneymindbackend.dto.*;
import dev.momory.moneymindbackend.exception.CustomException;
import dev.momory.moneymindbackend.jwt.JWTUtil;
import dev.momory.moneymindbackend.response.ResponseDTO;
import dev.momory.moneymindbackend.service.FinancesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FinancesController {

    private final FinancesService financesService;
    private final JWTUtil jwtUtil;

    /**
     * 금융 신규등록
     * @param addDTO 요청받은 파라미터
     * @param accessToken 요청 Header accessToken
     * @return 성공여부
     */
    @PostMapping("/api/finances")
    public ResponseDTO<?> addAccountOrCard (@Valid @RequestBody(required = false) AddAccountOrCardRequest addDTO
                                                    ,@RequestHeader(name = "access") String accessToken) {

        // 요청받은 DTO NULL check
        if (ObjectUtils.isEmpty(addDTO)) {
            log.warn("FinancesController.addAccountOrCard = {}", "addDTO is null");
            throw new CustomException(HttpStatus.BAD_REQUEST, "요청값이 제대로 입력되지 않았습니다. 확인해주세요.","ERR1001");
        }

        // access 체크
        if (!StringUtils.hasText(accessToken)) {
            log.warn("CategoryController.getCategoryDetails: ERR_TOKEN_EXPIRED");
            throw new CustomException(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다. 다시 로그인 해주세요.", "ERR_TOKEN_EXPIRED");
        }

        // access token 에서 userid 추출
        String userid = jwtUtil.getUserid(accessToken);
        addDTO.setUserid(userid);

        // 잔액 음수 여부 체크
        if (addDTO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("FinancesController.addAccountOrCard = {}", "addDTO is negative");
            throw new CustomException(HttpStatus.BAD_REQUEST, "잔액 0원이상 입력해주세요.", "ERR_BALANCE_NEGATIVE");
        }

        AddAccountOrCardResponse responseDTO = financesService.addAccountOrCard(addDTO);

        return ResponseDTO.successResponse(responseDTO, "account add success");
    }

    /**
     * 금융 상세조회
     * @param id 금융 고유 아이디
     * @param accessToken 엑세스 토큰
     * @return 조회 성공여부
     */
    @GetMapping("/api/finances/{id}")
    public ResponseDTO<?> getAccountOrCardDetails(@PathVariable(required = false) Long id, @RequestHeader(name = "access") String accessToken) {

        // parameter check
        if (id == null || id <= 0) {
            log.warn("FinancesController.getAccountOrCardDetails = {}", "id is null");
            throw new CustomException(HttpStatus.BAD_REQUEST, "상세조회중 오류가 발생하였습니다. 다시시도해주세요.", "ERR_ID_REQUIRED");
        }

        // access token 에서 userid 추출
        String userid = jwtUtil.getUserid(accessToken);

        // 상세조회
        GetAccountOrCardResponse responseDTO = financesService.getAccountOrCardDetails(id, userid);

        return ResponseDTO.successResponse(responseDTO, "상세조회 성공");
    }

    /**
     * 금융 수정
     * @param updateDTO 수정값
     * @param id 금융 고유 아이디
     * @param accessToken accessToken
     * @return
     */
    @PutMapping("/api/finances/{id}")
    public ResponseDTO<?> updateAccountOrCard (@Valid @RequestBody(required = false) UpdateAccountOrCardRequest updateDTO
                                                ,@PathVariable Long id
                                                ,@RequestHeader(name = "access") String accessToken) {

        // 1. updateDTO null check
        if (ObjectUtils.isEmpty(updateDTO)) {
            log.warn("FinancesController.updateAccountOrCard = {}", "updateDTO is null");
            throw new CustomException(HttpStatus.BAD_REQUEST, "요청값이 제대로 입력되지 않았습니다. 확인해주세요.","ERR1001");
        }

        // 2. id null or 0 체크
        if (id == null || id <= 0) {
            log.warn("FinancesController.updateAccountOrCard = {}", "id is null");
            throw new CustomException(HttpStatus.BAD_REQUEST, "요청값이 제대로 입력되지 않았습니다. 확인해주세요.","ERR1002");
        }

        // token check
        if (!StringUtils.hasText(accessToken)) {
            log.warn("CategoryController.updateAccountOrCard: ERR_TOKEN_EXPIRED");
            throw new CustomException(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다. 다시 로그인 해주세요.", "ERR_TOKEN_EXPIRED");
        }

        // 잔액 음수 여부 체크
        if (updateDTO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("FinancesController.updateAccountOrCard = {}", "updateDTO is negative");
            throw new CustomException(HttpStatus.BAD_REQUEST, "잔액 0원이상 입력해주세요.", "ERR_BALANCE_NEGATIVE");
        }

        // userid 추출
        String userid = jwtUtil.getUserid(accessToken);

        // 서비스 전달
        UpdateAccountOrCardResponse responseDTO = financesService.updateAccountOrCard(updateDTO, id, userid);

        return ResponseDTO.successResponse(responseDTO, id + " - update success");
    }

    @GetMapping("/api/finances")
    public ResponseDTO<?> getAccountOrCardList (@RequestParam(required = false, defaultValue = "0") int page,
                                                @RequestParam(required = false, defaultValue = "10") int size,
                                                @RequestHeader(name = "access") String accessToken) {
        PageRequest pageable = PageRequest.of(page, size);

        // token check
        if (!StringUtils.hasText(accessToken)) {
            log.warn("CategoryController.getAccountOrCardList: ERR_TOKEN_EXPIRED");
            throw new CustomException(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다. 다시 로그인 해주세요.", "ERR_TOKEN_EXPIRED");
        }

        // userid 추출
        String userid = jwtUtil.getUserid(accessToken);

        Page<GetAccountOrCardListResponse> responseDTO = financesService.getAccountOrCardList(pageable, userid);

        return ResponseDTO.successResponse(responseDTO, "list success");
    }

}
