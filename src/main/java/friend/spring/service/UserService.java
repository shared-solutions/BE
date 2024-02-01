package friend.spring.service;

import friend.spring.domain.User;

import java.util.Optional;

public interface UserService {
    User findMyPage(Long id);
    void checkUser(Boolean flag);

    Integer pointCheck(Long id);
}
