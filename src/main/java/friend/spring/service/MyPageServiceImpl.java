package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.PostHandler;
import friend.spring.converter.MyPageConverter;
import friend.spring.domain.Category;
import friend.spring.domain.File;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.enums.S3ImageType;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.repository.CategoryRepository;
import friend.spring.repository.PostRepository;
import friend.spring.repository.PostScrapRepository;
import friend.spring.repository.UserRepository;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.MyPageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MyPageServiceImpl implements MyPageService{

    private final UserRepository userRepository;
    private final PostService postService;
    private final UserService userService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostScrapRepository postScrapRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Service s3Service;

    @Override
    public void checkPost(Boolean flag) {
        if (!flag) {
            throw new PostHandler(ErrorStatus.POST_CATGORY_NOT_FOUND);
        }
    }
    //저장한 게시물
    @Override
    public List<Category> getCategoryList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        List<Post_scrap> scrapList = user.getPostScrapList();
        if (scrapList.isEmpty()) {
            postService.checkPostScrap(false);
        }
        List<Category> categoryList = scrapList.stream()
                .map(Post_scrap::getPost).filter(Objects::nonNull)
                .map(Post::getCategory).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        return categoryList;
        }

    @Override
    public Page<Post> getAllPostList(Long userId, Integer page, Integer sort) {
        if (sort == 0){
            Page<Post> scrapListByView = postScrapRepository.findPostsByUserIdOrderByPostViewDesc(userId, PageRequest.of(page, 10));
            return scrapListByView;
        }
        else if (sort == 1){
            Page<Post> scrapListByRecent = postScrapRepository.findPostsByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, 10));
            return scrapListByRecent;
        }
        else return null;
    }

    @Override
    @Transactional
    public void editUserImage(MultipartFile file, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        User user = optionalUser.get();
        File newFile = s3Service.uploadSingleImage(file, S3ImageType.USER, user, null);
        user.setFile(newFile);
    }
}
