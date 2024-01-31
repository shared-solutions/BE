package friend.spring.service;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.repository.PostRepository;
import friend.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    @Override
    public void checkPost(Boolean flag) {
        if (!flag) {
            throw new UserHandler(ErrorStatus.POST_NOT_FOUND);
        }
    }

    //한 유저의 모든 질문글
    @Override
    public Page<Post> getMyPostList(Long userId, Integer page) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

        User myUser = user.get();

        Page<Post> userPage = postRepository.findAllByMyPost(myUser, PageRequest.of(page, 5));
        return userPage;
    }

}
