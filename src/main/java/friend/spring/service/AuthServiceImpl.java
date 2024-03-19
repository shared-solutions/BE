package friend.spring.service;

import friend.spring.converter.UserConverter;
import friend.spring.domain.User;
import friend.spring.OAuth.KakaoProfile;
import friend.spring.OAuth.OAuthToken;
import friend.spring.OAuth.provider.KakaoAuthProvider;
import friend.spring.repository.UserRepository;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.TokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KakaoAuthProvider kakaoAuthProvider;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
//    private final RefreshTokenService refreshTokenService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public List<TokenDTO> kakaoLogin(String code) {
        OAuthToken oAuthToken = kakaoAuthProvider.requestToken(code);
        KakaoProfile kakaoProfile =
                kakaoAuthProvider.requestKakaoProfile(oAuthToken.getAccess_token());

        System.out.println(kakaoProfile.getKakao_account().getEmail());
        // 유저 정보 받기
        Optional<User> queryUser =
                userRepository.findByEmail(
                        kakaoProfile.getKakao_account().getEmail());
        System.out.println(queryUser.get().getEmail());
        System.out.println(queryUser.get().getBirth());
//         가입자 혹은 비가입자 체크해서 로그인 처리
        if (queryUser.isPresent()) {
            User user = queryUser.get();
            TokenDTO accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
            TokenDTO refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            redisTemplate.opsForValue().set("RT:" + user.getEmail(), refreshToken.getToken(), refreshToken.getTokenExpriresTime().getTime(), TimeUnit.MILLISECONDS);

            List<TokenDTO> tokenDTOList = new ArrayList<>();
            tokenDTOList.add(refreshToken);
            tokenDTOList.add(accessToken);

            return tokenDTOList;
        } else {
            User user = userRepository.save(UserConverter.KakaoUser(kakaoProfile));
            TokenDTO accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
            TokenDTO refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            redisTemplate.opsForValue().set("RT:" + refreshToken, TimeUnit.MILLISECONDS);

            return (List<TokenDTO>) UserConverter.toOAuthResponse(accessToken, refreshToken, false, user);
        }
    }
}