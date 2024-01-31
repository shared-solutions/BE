package friend.spring.service;

import friend.spring.domain.Level;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.repository.LevelRepository;
import friend.spring.repository.PostRepository;
import friend.spring.repository.UserRepository;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LevelRepository levelRepository;
    @Override
    public User findMyPage(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }
        return user.get();
    }

    @Override
    public void checkUser(Boolean flag) {
        if (!flag) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }
    }

    @Override
    public Level nextLevel(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }
        Long curId = user.get().getLevel().getId();
        Long nxtId = curId + 1;
        Level nxtLevel = levelRepository.findById(nxtId).get();
        return nxtLevel;
    }

}
