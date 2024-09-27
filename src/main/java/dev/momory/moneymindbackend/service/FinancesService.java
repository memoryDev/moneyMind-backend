package dev.momory.moneymindbackend.service;

import dev.momory.moneymindbackend.dto.AddAccountOrCardRequest;
import dev.momory.moneymindbackend.dto.AddAccountOrCardResponse;
import dev.momory.moneymindbackend.dto.GetAccountOrCardResponse;
import dev.momory.moneymindbackend.entity.Account;
import dev.momory.moneymindbackend.entity.User;
import dev.momory.moneymindbackend.exception.CustomException;
import dev.momory.moneymindbackend.repository.FinancesRepository;
import dev.momory.moneymindbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
