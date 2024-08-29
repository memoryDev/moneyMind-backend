package dev.momory.moneymindbackend.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 기본 OAuth2 사용자 정보를 로드 (소셜 로그인 제공자에서 사용자 정보를 조회)
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자를 요청한 소셜 로그인 서비스의 이름(예: kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 시 사용하는 사용자 정보의 식별자 키 (예: "id" 또는 "sub" 등등)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 가져온 사용자 정보를 기반으로 애플리케이션에서 사용하는 사용자 객체로 매핑
        // 사용자의 속성 정보를 변환하거나 필요한 추가 로직을 수행
        // 예: 사용자 저장, 권한 설정, 로깅 등

        // 최종적으로 처리된 OAuth2User 객체를 반환함
        return oAuth2User;
    }
}
