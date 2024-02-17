package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.PostHandler;
import friend.spring.converter.MyPageConverter;
import friend.spring.domain.*;
import friend.spring.domain.enums.RoleType;
import friend.spring.domain.enums.S3ImageType;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.repository.*;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.MyPageRequestDTO;
import friend.spring.web.dto.MyPageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final FileRepository fileRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Service s3Service;
    private final InquiryRepository inquiryRepository;
    private final NoticeRepository noticeRepository;
    private final TermRepository termRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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

        Optional<File> optionalFile = fileRepository.findByUserId(userId);
        File newFile = null;
        if (optionalFile.isPresent()) { // 이미 유저 프로필사진이 있는 경우 -> 기존 데이터 변경
            newFile = s3Service.editSingleImage(file, user);
        } else { // 유저 프로필 사진이 없는 경우 -> 새로 데이터 추가
            newFile = s3Service.uploadSingleImage(file, S3ImageType.USER, user, null);
        }
        user.setFile(newFile);
    }

    @Override
    public User getEditUserPage(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        return user;
    }

    @Override
    public User editUserName(Long userId, MyPageRequestDTO.ProfileEditNameReq profileEditNameReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        user.setNickname(profileEditNameReq.getNickName());
        return user;
    }

    @Override
    public User editUserEmail(Long userId, MyPageRequestDTO.ProfileEditEmailReq profileEditEmailReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        List<User> all = userRepository.findAll();
        all.forEach(eachUser -> {
                    if (eachUser.getEmail().equals(profileEditEmailReq.getChangeEmail())) {
                        throw new GeneralException(ErrorStatus.USER_EXISTS_EMAIL);
                    }
                });
        user.setEmail(profileEditEmailReq.getChangeEmail());
        return user;
    }

    @Override
    public User editUserPhone(Long userId, MyPageRequestDTO.ProfileEditPhoneReq profileEditPhoneReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        user.setPhone(profileEditPhoneReq.getPhone());
        return user;
    }

    @Override
    public User editUserPassword(Long userId, MyPageRequestDTO.ProfileEditPasswordReq profileEditPasswordReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        if (!encoder.matches(profileEditPasswordReq.getCurPassword(), user.getPassword())){
            throw new GeneralException(ErrorStatus.PASSWORD_INCORRECT);
        }
        if (!profileEditPasswordReq.getChangePassword().equals(profileEditPasswordReq.getCheckPassword())){
            throw new GeneralException(ErrorStatus.PASSWORD_CHECK_INCORRECT);
        }
        String encode = encoder.encode(profileEditPasswordReq.getChangePassword());
        user.setPassword(encode);
        return user;
    }

    @Override
    public User editUserSecurity(Long userId, MyPageRequestDTO.ProfileEditSecurityReq profileEditSecurityReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        List<User> all = userRepository.findAll();
        all.forEach(eachUser -> {
            if (eachUser.getEmail().equals(profileEditSecurityReq.getChangeEmail())) {
                throw new GeneralException(ErrorStatus.USER_EXISTS_EMAIL);
            }
        });
        user.setEmail(profileEditSecurityReq.getChangeEmail());
        return user;
    }

    @Override
    public Inquiry createInquiry(Long userId, MyPageRequestDTO.MyInquiryReq myInquiryReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Inquiry inquiry = MyPageConverter.toInquiry(myInquiryReq, user);
        return inquiryRepository.save(inquiry);
    }

    @Override
    public Page<Post> getCategoryDetailList(Long userId, Long categoryId, Integer page) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_CATGORY_NOT_FOUND));
        Page<Post> detailList = postRepository.findCategoryDetail(userId, categoryId, PageRequest.of(page, 10));
        return detailList;
    }

    @Override
    public Category getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_CATGORY_NOT_FOUND));
        return category;
    }

    @Override
    public Page<Notice> getNoticeList(Long userId, Integer page) {
        User admin = checkAdmin(userId);
        Page<Notice> noticeList = noticeRepository.findAllByUser(admin, PageRequest.of(page, 10));
        return noticeList;
    }

    @Override
    public User checkAdmin(Long adminId) {
        User user = userRepository.findById(adminId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        if (user.getRole() == RoleType.USER){
            throw new GeneralException(ErrorStatus.NOT_ADMIN);
        }
        return user;
    }

    @Override
    public Notice getNoticeDetail(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GeneralException(ErrorStatus.NOTICE_NOT_FOUND));
        return notice;
    }

    @Override
    public Term getTerm(Long userId) {
        User admin = checkAdmin(userId);
        Long termId = 1L;
        Term term = termRepository.findById(termId).get();
        return term;
    }

    @Override
    public Term getPrivacy(Long userId) {
        User admin = checkAdmin(userId);
        Long privacyId = 1L;
        Term privacy = termRepository.findById(privacyId).get();
        return privacy;
    }


}
