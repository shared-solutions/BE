package friend.spring.security;

import ch.qos.logback.core.status.ErrorStatus;
import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.domain.User;
import friend.spring.repository.UserRepository;
import friend.spring.web.dto.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static friend.spring.apiPayload.code.status.ErrorStatus.*;

@Slf4j
@RequiredArgsConstructor
@Component
@PropertySource(value = {"/application.yml"})
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final RedisTemplate redisTemplate;


    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT Access 토큰 생성
    public TokenDTO createAccessToken(String email) {
        // 토큰 유효시간 30분
        long tokenValidTime =48 * 60 * 60 * 1000L;

        Optional<User> user = userRepository.findByEmail(email);

        Claims claims = Jwts.claims().setSubject(email); // JWT payload에 저장되는 정보단위

        user.ifPresent(value -> claims.put("id", value.getId()));


        Date now = new Date();
        Date expiresTime = new Date(now.getTime() + tokenValidTime);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .claim("types", "atk")
                //.claim("userIdx",user.get().getUserIdx())
                //.claim("role", user.get().getRole())
                .compact();

        return new TokenDTO(String.valueOf(TokenType.atk), token, expiresTime);
    }

    // JWT Refresh 토큰 생성
    public TokenDTO createRefreshToken(String email) {
        // Refresh 토큰 유효시간 2주
        long tokenValidTime = 2 * 7 * 24 * 60 * 60 * 1000L;
        Optional<User> user = userRepository.findByEmail(email);

        Claims claims = Jwts.claims().setSubject(email); // JWT payload에 저장되는 정보단위

        user.ifPresent(value -> claims.put("id", value.getId()));

        Date now = new Date();
        Date expiresTime = new Date(now.getTime() + tokenValidTime); // 토큰 만료 시간
        String token = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(expiresTime) // Expire Time 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature 에 들어갈 secretkey 값 설정
                .claim("types", "rtk")
                .compact();
        return new TokenDTO(String.valueOf(TokenType.rtk),token, expiresTime);
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getTokenSub(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원정보 추출 - email (payload의 subject)
    public String getTokenSub(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request 의 Header 에서 access token 값 추출 "atk" : "token--"
    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader("atk");
    }

    // 토큰 재발급 때 Header에 rtk를 넣어 요청, 나머지 경우 atk 사용
    public String resolveToken(HttpServletRequest request) {
        if (request.getHeader("rtk") != null){
            return request.getHeader("rtk");
        } else {
            return request.getHeader("atk");
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken); // 토큰의  payload(claim)            // Access 토큰의 경우 redis 까지 검사
            if  (claims.getBody().get("types").equals("atk"))  {
                Object isLogOut = redisTemplate.opsForValue().get(jwtToken); // token 을 key 로 value 가져옴 (null 이면 유효 토큰, logout 이면 유효하지 않은 토큰)
                // 로그인 시 redis 에 email : refreshtoken 형태로 저장
                // 로그아웃 시 redis 에 accesstoken : logout 형태로 저장
                if (isLogOut != null) {
                    return false;
                }
                return !claims.getBody().getExpiration().before(new Date());// 만료안됐으면 true, 만료됐으면 false
            } else {
                // Refresh 토큰 유효성 검사
                return !claims.getBody().getExpiration().before(new Date()); // 만료안됐으면 true, 만료됐으면 false
            }
        }catch (Exception e) {
            return false;
        }
    }

    // 토큰 만료 시간
    public Date getExpireTime(String jwtToken) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        return claims.getBody().getExpiration();
    }
    // 토큰에서 회원정보 추출 - userIdx 추출
    public Long getCurrentUser(HttpServletRequest request) throws GeneralException { // userIdx 가져오기
        String jwtToken = resolveAccessToken(request); // Request의 header에서 Access 토큰 추출
        if(!validateToken(jwtToken)) {
            throw new GeneralException(INVALID_JWT);
        }

        Long userIdx = Long.valueOf(String.valueOf(Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody()
                .get("id")));

        return userIdx;
    }
//    public String getemail(String token) {
//        return getClaims(token).getBody().get("id", String.class);
//    }
//    private Jws<Claims> getClaims(String token) {
//        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//    }
}
