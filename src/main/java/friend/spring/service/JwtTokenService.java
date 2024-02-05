package friend.spring.service;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenService {
    Long JwtToId(HttpServletRequest request);
}
