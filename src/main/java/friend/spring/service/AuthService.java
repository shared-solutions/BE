package friend.spring.service;

import friend.spring.web.dto.TokenDTO;

import java.util.List;

public interface AuthService {

    List<TokenDTO> kakaoLogin(String code);

//    void logout(Long userId);
}
