package friend.spring.service;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.PostHandler;
import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.repository.CategoryRepository;
import friend.spring.repository.PostScrapRepository;
import friend.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageServiceImpl implements MyPageService{

    private final UserRepository userRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final PostScrapRepository postScrapRepository;
    @Override
    public void checkPost(Boolean flag) {
        if (!flag) {
            throw new PostHandler(ErrorStatus.POST_CATGORY_NOT_FOUND);
        }
    }
    @Override
    public void checkScrapPost(Boolean flag) {
        if (!flag) {
            throw new PostHandler(ErrorStatus.POST_SAVED_NOT_FOUND);
        }
    }

    //저장한 게시물
    @Override
    public List<Category> getScrapList(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            userService.checkUser(false);
        }
        User user = optionalUser.get();
        List<Post_scrap> postScrapList = user.getPostScrapList().stream()
                .map(postScrap -> {
                    return postScrapRepository.findById(postScrap.getId()).orElseThrow(() -> new PostHandler(ErrorStatus.POST_SAVED_NOT_FOUND));
                }).collect(Collectors.toList());
        if (postScrapList.isEmpty()){
            checkScrapPost(false);
        }

        List<Category> categoryList = postScrapList.stream().map(postScrap -> {
            return postScrap.getPost().getPostCategory();
        }).collect(Collectors.toList());
        return categoryList;
    }
}
