package friend.spring.service;

import friend.spring.domain.Level;
import friend.spring.domain.User;
import friend.spring.web.dto.UserRequestDTO;


public interface UserService {

    User joinUser(UserRequestDTO.UserJoinRequest userJoinRequest);

    User findMyPage(Long id);
    void checkUser(Boolean flag);
    Level nextLevel(Long id);
}
