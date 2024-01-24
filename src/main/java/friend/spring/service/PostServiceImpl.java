package friend.spring.service;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    @Override
    public void checkPost(Boolean flag) {
        if (!flag) {
            throw new UserHandler(ErrorStatus.POST_NOT_FOUND);
        }
    }
}
