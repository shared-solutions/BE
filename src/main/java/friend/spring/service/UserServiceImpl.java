package friend.spring.service;

import friend.spring.domain.User;
import friend.spring.repository.UserRepository;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }
    @Override
    public void checkUser(Boolean flag) {
        if (!flag) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }
    }
}
