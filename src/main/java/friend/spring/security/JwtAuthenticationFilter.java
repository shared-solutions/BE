package friend.spring.security;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("필터 실행");
        // Request Header에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        // validateToken으로 토큰 유효성 검사
        if(token != null && jwtTokenProvider.validateToken(token)) {

            // Redis에 해당 access token logout여부를 확인
            String isLogout = (String) redisTemplate.opsForValue().get(token);

            // 로그아웃이 없는(되어 있지 않은) 경우 해당 토큰은 정상적으로 작동하기
            if (ObjectUtils.isEmpty(isLogout)) {

                // token이 유효하면 인증 객체 생성
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                // SecurityContextHolder.getContext() : 시큐리티의 session 공간
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request, response);

    }
}
