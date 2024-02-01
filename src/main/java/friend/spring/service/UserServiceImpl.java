package friend.spring.service;


import friend.spring.converter.UserConverter;
import friend.spring.domain.User;
import friend.spring.repository.UserRepository;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.web.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static friend.spring.apiPayload.code.status.ErrorStatus.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    //private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findMyPage(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
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
    public User joinUser(UserRequestDTO.UserJoinRequest userJoinRequest){

        User newUser = UserConverter.toUser(userJoinRequest);

        return userRepository.saveAndFlush(newUser);
    }

}


