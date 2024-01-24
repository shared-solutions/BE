package friend.spring.service;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public void checkUser(Boolean flag) {
        if (!flag) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }
    }
}
