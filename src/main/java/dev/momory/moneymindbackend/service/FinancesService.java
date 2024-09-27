package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.dto.*;
import dev.momory.moneymindbackend.entity.Account;
import dev.momory.moneymindbackend.entity.User;
import dev.momory.moneymindbackend.exception.CustomException;
import dev.momory.moneymindbackend.repository.FinancesRepository;
import dev.momory.moneymindbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinancesService {

    private final UserRepository userRepository;
    private final FinancesRepository financesRepository;

    public AddAccountOrCardResponse addAccountOrCard (AddAccountOrCardRequest addDTO) {

        // userid check
        if (!StringUtils.hasText(addDTO.getUserid())) {
            log.warn("FinancesService.addAccountOrCard = {}", "user login fail - ERR_LOGIN_FAIL");
            throw new CustomException(HttpStatus.BAD_REQUEST, "로그인을 다시 진행해주세요.", "ERR_LOGIN_FAIL");
        }

        // 1. user 조회
        User user = userRepository.findByUserid(addDTO.getUserid());
        System.out.println(addDTO.getUserid());
        if (ObjectUtils.isEmpty(user)) {
            log.warn("FinancesService.addAccountOrCard = {}", "user login fail - ERR_LOGIN_JOIN_FAIL");
            throw new CustomException(HttpStatus.BAD_REQUEST, "로그인을 다시 진행해주세요.", "ERR_LOGIN_JOIN_FAIL");
        }

        // 2. DTO -> ENTITY
        Account entity = addDTO.toEntity();

        // 3. 조회된 유저 추가
        entity.addUser(user);

        // 4. 데이터 저장
        Account savedEntity = financesRepository.save(entity);

        // ENTITY -> DTO
        AddAccountOrCardResponse responseDTO = new AddAccountOrCardResponse().toDTO(savedEntity);

        return responseDTO;
    }

    public GetAccountOrCardResponse getAccountOrCardDetails(Long id, String userid) {

        // 게시글 조회
        Account entity = financesRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글 입니다.", "ERR_ENTITY_NOT_FOUND"));

        // entity -> dto
        GetAccountOrCardResponse responseDTO = new GetAccountOrCardResponse().toDTO(entity, userid);

        return responseDTO;
    }

    public UpdateAccountOrCardResponse updateAccountOrCard(UpdateAccountOrCardRequest updateDTO, Long id, String userid) {
        // 게시글 조회
        Account entity = financesRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글 입니다.", "ERR_ENTITY_NOT_FOUND"));

        // 본인글 인지 체크
        if (!userid.equals(entity.getUser().getUserid())) {
            log.warn("FinancesService.updateAccountOrCard = {} - {}", "userid", "ERR_NOT_AUTHORIZED");
            throw new CustomException(HttpStatus.FORBIDDEN, "본인이 작성한 게시글만 수정하실수 있습니다.", "ERR_NOT_AUTHORIZED");
        }

        // 조회된 게시글 수정
        entity.updateAccount(updateDTO);

        // 수정후 save 실행
        Account savedEntity = financesRepository.save(entity);

        // 응답 DTO 변환후 반환
        return new UpdateAccountOrCardResponse().toDTO(savedEntity);
    }

    public Page<GetAccountOrCardListResponse> getAccountOrCardList(Pageable pageable, String userid) {
        return financesRepository.getAccountOrCardList(pageable, userid);
    }
}
