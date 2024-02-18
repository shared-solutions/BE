package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.converter.UserConverter;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.domain.Level;
import friend.spring.domain.User;
import friend.spring.repository.LevelRepository;
import friend.spring.repository.UserRepository;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.TokenDTO;
import friend.spring.web.dto.UserRequestDTO;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static friend.spring.apiPayload.code.status.ErrorStatus.*;
import static java.util.regex.Pattern.matches;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final LevelRepository levelRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z]+)(?=.*\\d)(?=.*[!@#$%^&*]).{8,64}$";

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User findMyPage(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserHandler(USER_NOT_FOUND);
        }
        return user.get();
    }

    @Override
    public void checkUser(Boolean flag) {
        if (!flag) {
            throw new UserHandler(USER_NOT_FOUND);
        }
    }

    public User joinUser(UserRequestDTO.UserJoinRequest userJoinRequest) {//회원가입
        Optional<User> user = userRepository.findByEmail(userJoinRequest.getEmail());

        if (user.isPresent()) {
            throw new UserHandler(USER_EXISTS_EMAIL);
        }


        String encodedPw = encoder.encode(userJoinRequest.getPassword());

        Level initLevel = levelRepository.findById(Long.valueOf(1)).get();
        User newUser = UserConverter.toUser(userJoinRequest, encodedPw, initLevel);

        return userRepository.saveAndFlush(newUser);
    }

    @Override
    public Level nextLevel(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserHandler(USER_NOT_FOUND);
        }
        Long curId = user.get().getLevel().getId();
        Long nxtId = curId + 1;
        Level nxtLevel = levelRepository.findById(nxtId).get();
        return nxtLevel;
    }

    @Override
    public List<TokenDTO> reissue(HttpServletRequest request) {
        String rtk = request.getHeader("rtk");
        System.out.println("reissue함수실행 rtk: " + rtk);

        // refresh token 유효성 검증
        if (!jwtTokenProvider.validateToken(rtk))
            throw new GeneralException(INVALID_JWT);

        String email = jwtTokenProvider.getTokenSub(rtk);
        System.out.println("rtk subject인 email: " + email);

        // Redis에서 email 기반으로 저장된 refresh token 값 가져오기
        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + email);

        if (refreshToken == null) {
            throw new GeneralException(RTK_INCORREXT);
        }

        if (!refreshToken.equals(rtk)) {
            throw new GeneralException(RTK_INCORREXT);
        }

        // refresh token 유효할 경우 새로운 토큰 생성
        List<TokenDTO> tokenDTOList = new ArrayList<>();
        TokenDTO newRefreshToken = jwtTokenProvider.createRefreshToken(email);
        TokenDTO newAccessToken = jwtTokenProvider.createAccessToken(email);
        tokenDTOList.add(newRefreshToken);
        tokenDTOList.add(newAccessToken);
        System.out.println("Access Token, Refresh Token 재발행: " + tokenDTOList);

        // Redis에 refresh token 업데이트
        redisTemplate.opsForValue().set("RT:" + email, newRefreshToken.getToken(), newRefreshToken.getTokenExpriresTime().getTime(), TimeUnit.MILLISECONDS);

        return tokenDTOList;
    }

    //로그인
    public List<TokenDTO> login(UserRequestDTO.UserLoginRequest userLoginRequest) throws GeneralException {
        User user = userRepository.findByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new GeneralException(USERS_NOT_FOUND_EMAIL));//가입 안된 이메일
        if (!encoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new GeneralException(PASSWORD_INCORRECT); //비밀번호 불일치
        }
        //토큰발급
        TokenDTO accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        TokenDTO refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // login 시 Redis에 RT:gominchingy@gmail.com(key): --refresh token실제값--(value) 형태로 refresh 토큰 저장하기
        // opsForValue() : set을 통해 key,value값 저장하고 get(key)통해 value가져올 수 있음.
        // refreshToken.getTokenExpriresTime().getTime() : 리프레시 토큰의 만료시간이 지나면 해당 값 자동 삭제
        redisTemplate.opsForValue().set("RT:" + user.getEmail(), refreshToken.getToken(), refreshToken.getTokenExpriresTime().getTime(), TimeUnit.MILLISECONDS);

        List<TokenDTO> tokenDTOList = new ArrayList<>();
        tokenDTOList.add(accessToken);
        tokenDTOList.add(refreshToken);
        System.out.println(tokenDTOList);

        return tokenDTOList;
    }

    @Override
    // 로그아웃 - userIdx
    public String logout(HttpServletRequest request) {

        Long userIdx = jwtTokenProvider.getCurrentUser(request);

        System.out.println("getCurrentUser()로 가져온 userIdx : " + userIdx);
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));

        // Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제
        if (redisTemplate.opsForValue().get("RT:" + user.getEmail()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + user.getEmail());
        }
        // 해당 AccessToken 유효시간 가지고 와서 저장하기
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        Long expiration = jwtTokenProvider.getExpireTime(accessToken).getTime();
        // Redis 에 --accesstoken--(key) : logout(value) 로 저장, token 만료시간 지나면 자동 삭제
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return "로그아웃 성공";
    }

    @Override
    public Integer pointCheck(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("\"" + id + "\"해당 유저가 없습니다"));
        return user.getPoint();
    }

    private boolean isValidPassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    //비밀번호 변경
    public User updatePassword(String email, UserRequestDTO.PasswordUpdateReq passwordUpdateReq) throws GeneralException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        String newPassword = passwordUpdateReq.getNewPassword();

        if (!isValidPassword(newPassword)) {
            throw new GeneralException(INVALID_PASSWORD_FORMAT);
        }
        if (!passwordUpdateReq.getNewPassword().equals(passwordUpdateReq.getNewPasswordCheck())) {
            throw new GeneralException(ErrorStatus.PASSWORD_CHECK_INCORRECT);
        }

                // 새로운 비밀번호가 null이 아닌 경우, 사용자의 비밀번호를 새로운 값으로 업데이트
        if (newPassword != null) {
            user.setPassword(encoder.encode(newPassword));
        }
        userRepository.save(user);
        return user;
    }
    // Request 의 Header 에서 email 값 추출 "email" : "gominchingu@gmail.com"
    public String getEmail(HttpServletRequest request) {
        return request.getHeader("email");
    }
}
//
//    // Request 의 Header 에서 access token 값 추출 "atk" : "token--"
//    public String resolveAccessToken(HttpServletRequest request) {
//        return request.getHeader("atk");
//    }

