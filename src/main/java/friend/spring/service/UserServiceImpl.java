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
    public Integer pointCheck(Long id) {
        User user=userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("\""+id+"\"해당 유저가 없습니다"));
        return user.getPoint();
    }
}
