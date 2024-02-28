package friend.spring.service;

import friend.spring.domain.Level;
import friend.spring.domain.User;
import friend.spring.web.dto.MyPageRequestDTO;
import friend.spring.web.dto.TokenDTO;
import friend.spring.web.dto.UserRequestDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService {

    User joinUser(UserRequestDTO.UserJoinRequest userJoinRequest);
    List<TokenDTO> login(UserRequestDTO.UserLoginRequest userLoginRequest);
    User findMyPage(HttpServletRequest httpServletRequest);
    void checkUser(Boolean flag);
    Integer pointCheck(Long id);
    Level nextLevel(HttpServletRequest request);
    String logout(HttpServletRequest request);
    List<TokenDTO> reissue(HttpServletRequest request);
    User updatePassword(String email, UserRequestDTO.PasswordUpdateReq passwordUpdateReq);
    String getEmail(HttpServletRequest request);
}
