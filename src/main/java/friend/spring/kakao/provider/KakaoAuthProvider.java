package friend.spring.kakao.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.kakao.dto.response.KakaoProfile;
import friend.spring.kakao.dto.response.OAuthToken;
import friend.spring.security.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Value;
import friend.spring.repository.UserRepository;
import friend.spring.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoAuthProvider {

    private final PrincipalDetailService principalDetailService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
//    private final RefreshTokenService refreshTokenService;

    @Value("${kakao.auth.client}")
    private String client;

    @Value("${kakao.auth.redirect-uri}")
    private String redirect;

    @Value("${kakao.auth.secret_key}")
    private String secretKey;

    // code로 access 토큰 요청하기
    public OAuthToken requestToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("redirect_uri", redirect);
        params.add("secret_key", secretKey);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://kauth.kakao.com/oauth/token",
                        HttpMethod.POST,
                        kakaoTokenRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST_INFO);
        }

        return oAuthToken;
    }

    // Token으로 정보 요청하기
    public KakaoProfile requestKakaoProfile(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://kapi.kakao.com/v2/user/me",
                        HttpMethod.POST,
                        kakaoProfileRequest,
                        String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        try {
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ErrorStatus.INVALID_REQUEST_INFO);
        }

        return kakaoProfile;
    }
}
