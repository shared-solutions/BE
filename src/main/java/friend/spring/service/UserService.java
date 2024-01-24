package friend.spring.service;

import friend.spring.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUser(Long id);
}
