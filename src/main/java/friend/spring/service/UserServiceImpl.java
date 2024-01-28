package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.domain.User;
import friend.spring.repository.UserRepository;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

import static friend.spring.apiPayload.code.status.ErrorStatus.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findMyPage(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
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
}
