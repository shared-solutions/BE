package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.PostHandler;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.converter.PostConverter;
import friend.spring.domain.*;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.S3ImageType;
import friend.spring.domain.mapping.Post_like;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.repository.*;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static friend.spring.apiPayload.code.status.ErrorStatus.*;
import static friend.spring.domain.enums.PostType.*;
import static friend.spring.domain.enums.PostVoteType.*;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final General_PollRepository generalPollRepository;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final Gauge_PollRepository gaugePollRepository;
    private final Card_PollRepository cardPollRepository;
    private final PointRepository pointRepository;
    private final CategoryRepository categoryRepository;

    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PostScrapRepository postScrapRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    private final S3Service s3Service;

    @Override
    public void checkPost(Boolean flag) {
        if (!flag) {
            throw new PostHandler(POST_NOT_FOUND);
        }
    }

    @Override
    public void checkPostWriterUser(Boolean flag) {
        if (!flag) {
            throw new PostHandler(POST_NOT_CORRECT_USER);
        }
    }

    @Override
    public Boolean checkPoint(PostRequestDTO.AddPostDTO request, User user) {
        if (request.getPoint() > user.getPoint()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void checkPostLike(Boolean flag) {
        if (!flag) {
            throw new PostHandler(POST_LIKE_NOT_FOUND);
        } else {
            throw new PostHandler(POST_LIKE_DUPLICATE);
        }
    }

    @Override
    public void checkPostScrap(Boolean flag) {
        if (!flag) {
            throw new PostHandler(POST_SCRAP_NOT_FOUND);
        } else {
            throw new PostHandler(POST_SCRAP_DUPLICATE);
        }
    }

    @Override
    @Transactional
    public Post joinPost(PostRequestDTO.AddPostDTO request, HttpServletRequest request2) {
        Long userId = jwtTokenProvider.getCurrentUser(request2);

        Post newPost = PostConverter.toPost(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));
        newPost.setUser(user);

        // 글 첨부파일 사진 저장
        if (request.getFileBase64List() != null) {
            s3Service.uploadPostImagesBase64(request.getFileBase64List(), S3ImageType.POST, newPost);
        }
        LocalDateTime deadline = request.getDeadline();
        if (request.getDeadline() == null) {
            deadline = LocalDateTime.now().plusHours(1);
        }

        //일반 투표 api
        if (newPost.getPostType() == VOTE) {
            newPost.setCategory(categoryRepository.findByName(request.getCategory()));

        }

        if (newPost.getPostType() == VOTE && newPost.getVoteType() == GENERAL) {
            //포인트 차감 관련 코드
            if (request.getPoint() != null) {
                if (!checkPoint(request, user)) {
                    throw new GeneralException(NOT_ENOUGH_POINT);
                }
                user.setPoint(user.getPoint() - request.getPoint());
                Point newPoint = Point.builder()
                        .amount(request.getPoint() * -1) // 차감이므로 -1 곱해서 음수로 변환
                        .content("일반 투표 작성에 대한 " + request.getPoint() + " 포인트 차감")
                        .build();
                newPoint.setUser(user);
                pointRepository.save(newPoint);
            }

            General_poll generalPoll = General_poll.builder()
                    .pollTitle(request.getPollTitle())
                    .deadline(deadline)
                    .build();
            if (request.getMultipleChoice() != null) {
                generalPoll.setMultipleChoice(request.getMultipleChoice());
            }
            newPost.setGeneralPoll(generalPoll);
            generalPollRepository.save(generalPoll);

        }
//카드 투표 api
        if (newPost.getPostType() == VOTE && newPost.getVoteType() == CARD) {
            //포인트 차감 관련 코드
            if (request.getPoint() != null) {
                if (!checkPoint(request, user)) {
                    throw new GeneralException(NOT_ENOUGH_POINT);
                }
                user.setPoint(user.getPoint() - request.getPoint());
                Point newPoint = Point.builder()
                        .amount(request.getPoint() * -1)
                        .content("게이지 투표 등록에 대한 " + request.getPoint() + " 포인트 차감")
                        .build();
                newPoint.setUser(user);
                pointRepository.save(newPoint);
            }

            if (!checkPoint(request, user) && request.getPoint() != null) {
                throw new GeneralException(NOT_ENOUGH_POINT);
            }

            Card_poll cardPoll = Card_poll.builder()
                    .pollTitle(request.getPollTitle())
                    .deadline(deadline)
                    .build();
            if (request.getMultipleChoice() != null) {
                cardPoll.setMultipleChoice(request.getMultipleChoice());
            }
            newPost.setCardPoll(cardPoll);
            cardPollRepository.save(cardPoll);
        }
//게이지 투표 api
        if (newPost.getPostType() == VOTE && newPost.getVoteType() == GAUGE) {
            //포인트 차감 관련 코드
            if (request.getPoint() != null) {
                if (!checkPoint(request, user)) {
                    throw new GeneralException(NOT_ENOUGH_POINT);
                }
                user.setPoint(user.getPoint() - request.getPoint());
                Point newPoint = Point.builder()
                        .amount(request.getPoint() * -1)
                        .content("카드 투표 등록에 대한 " + request.getPoint() + " 포인트 차감")
                        .build();
                newPoint.setUser(user);
                pointRepository.save(newPoint);
            }

            if (!checkPoint(request, user) && request.getPoint() != null) {
                throw new GeneralException(NOT_ENOUGH_POINT);
            }

            Gauge_poll gaugePoll = Gauge_poll.builder()
                    .pollTitle(request.getPollTitle())
                    .gauge(0)
                    .deadline(deadline)
                    .build();

            newPost.setGaugePoll(gaugePoll);
            gaugePollRepository.save(gaugePoll);
        }

        if (newPost.getPostType() == REVIEW && request.getParent_id() != null) {
            Post parent = postRepository.findById(request.getParent_id())
                    .orElseThrow(() -> new GeneralException(POST_NOT_FOUND));
            if (!userId.equals(parent.getUser().getId())) {
                throw new GeneralException(POST_NOT_CORRECT_USER);
            }
            newPost.setParentPost(parent);
        }

        return postRepository.save(newPost);

    }

    @Override
    @Transactional
    public Candidate createCandidate(Long postId, CandidateRequestDTO.AddCandidateRequestDTO request, HttpServletRequest request2) throws IOException {
        Post newPost = postRepository.findById(postId).orElseThrow(() -> new GeneralException(POST_NOT_FOUND));

        Long userId = jwtTokenProvider.getCurrentUser(request2);
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(USER_NOT_FOUND));
        if (!newPost.getUser().equals(user)) { // 이 글을 쓴 사용자인지 검증
            this.checkPostWriterUser(false);
        }

        if (!(!request.getOptionString().isEmpty() && request.getOptionString().length() < 30)) {
            throw new GeneralException(CANDIDATE_TEXT_LIMIT);
        }

        Candidate candidate = Candidate.builder()
                .name(request.getOptionString())
                .build();

        candidateRepository.save(candidate);

        if (request.getOptionImg() != null) {
            File candidateFile = s3Service.uploadSingleImageBase64(request.getOptionImg(), S3ImageType.CANDIDATE, null, candidate);
            candidate.setFile(candidateFile);
        }

        // 일반 투표
        if (newPost.getPostType() == VOTE && newPost.getVoteType() == GENERAL) {
            General_poll generalPoll = generalPollRepository.findById(newPost.getGeneralPoll().getId()).orElseThrow(() -> new GeneralException(POST_GENERAL_POLL_NOT_FOUND));
            candidate.setGeneralPoll(generalPoll);
        }
        // 카드 투표
        else {
            Card_poll cardPoll = cardPollRepository.findById(newPost.getCardPoll().getId()).orElseThrow(() -> new GeneralException(POST_CARD_POLL_NOT_FOUND));
            candidate.setCardPoll(cardPoll);
        }

        return candidate;
    }

    @Override
    @Transactional
    public void editPost(Long postId, PostRequestDTO.PostEditReq request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(POST_NOT_FOUND));
        if (!user.getId().equals(post.getUser().getId())) {
            throw new GeneralException(POST_NOT_CORRECT_USER);
        }
        if (post.getIsFixed() >= 2) {
            throw new GeneralException(TOO_MUCH_FIXED);
        }
        if (!(request.getDeadline() == null)) {
            LocalDateTime currentDeadLine;
            if (post.getVoteType() == GENERAL) {
                currentDeadLine = post.getGeneralPoll().getDeadline();
                long daysUntilDeadline = ChronoUnit.DAYS.between(currentDeadLine, request.getDeadline());
                if (request.getDeadline().isBefore(LocalDateTime.now())) {
                    throw new GeneralException(DEADLINE_LIMIT);
                }
                if (!(daysUntilDeadline <= 30)) {
                    throw new GeneralException(DEADLINE_LIMIT);
                }
                post.getGeneralPoll().setDeadline(request.getDeadline());
            }
            if (post.getVoteType() == CARD) {
                currentDeadLine = post.getCardPoll().getDeadline();
                long daysUntilDeadline = ChronoUnit.DAYS.between(currentDeadLine, request.getDeadline());
                if (request.getDeadline().isBefore(LocalDateTime.now())) {
                    throw new GeneralException(DEADLINE_LIMIT);
                }
                if (!(daysUntilDeadline <= 30)) {
                    throw new GeneralException(DEADLINE_LIMIT);
                }
                post.getCardPoll().setDeadline(request.getDeadline());
            }
            if (post.getVoteType() == GAUGE) {
                currentDeadLine = post.getGaugePoll().getDeadline();
                long daysUntilDeadline = ChronoUnit.DAYS.between(currentDeadLine, request.getDeadline());
                if (request.getDeadline().isBefore(LocalDateTime.now())) {
                    throw new GeneralException(DEADLINE_LIMIT);
                }
                if (!(daysUntilDeadline <= 30)) {
                    throw new GeneralException(DEADLINE_LIMIT);
                }
                post.getGaugePoll().setDeadline(request.getDeadline());
            }
        }
        if (!request.getVoteOnGoing()) {
            if (post.getVoteType() == GENERAL) {
                post.getGeneralPoll().setVoteOnGoing(false);
            }
            if (post.getVoteType() == CARD) {
                post.getCardPoll().setVoteOnGoing(false);
            }
            if (post.getVoteType() == GAUGE) {
                post.getGaugePoll().setVoteOnGoing(false);
            }
        }
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setIsFixed(post.getIsFixed() + 1);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(POST_NOT_FOUND));
        if (!user.getId().equals(post.getUser().getId())) {
            throw new GeneralException(POST_NOT_CORRECT_USER);
        }
        post.setStateDel();
    }


    //한 유저의 모든 질문글
    @Override
    public Page<Post> getMyPostList(Long userId, Integer page) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

        User myUser = user.get();

        Page<Post> userPage = postRepository.findAllByUser(myUser, PageRequest.of(page, 5));
        return userPage;
    }

    @Override
    public Post_like likePost(Long postId, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            this.checkPost(false);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Post post = optionalPost.get();
        User user = optionalUser.get();

        Post_like post_like = PostConverter.toPostLike(post, user);
        Optional<Post_like> optionalPost_like = postLikeRepository.findByPostIdAndUserId(postId, userId);
        if (!optionalPost_like.isEmpty()) {
            this.checkPostLike(true);
        }

        return postLikeRepository.save(post_like);
    }

    @Override
    public void dislikePost(Long postId, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            this.checkPost(false);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Optional<Post_like> optionalPost_like = postLikeRepository.findByPostIdAndUserId(postId, userId);
        if (optionalPost_like.isEmpty()) {
            this.checkPostLike(false);
        }

        Post_like post_like = optionalPost_like.get();
        postLikeRepository.delete(post_like);
    }

    @Override
    public PostResponseDTO.PollPostGetListDTO getBestPosts(Integer page, Integer size, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        LocalDateTime minusDays = LocalDateTime.now().minusDays(7); // 7일 이내
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "point"));
        Page<Post> bestPostPage = postRepository.findByPostTypeAndStateAndCreatedAtAfter(PostType.VOTE, PostState.POSTING, minusDays, pageable);

        return PostConverter.pollPostGetListDTO(bestPostPage, userId);
    }

    @Override
    public PostResponseDTO.PollPostGetListDTO getRecentPosts(Integer page, Integer size, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));

        LocalDateTime minusDays = LocalDateTime.now().minusDays(7); // 7일 이내(임시)
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> recentPostPage = postRepository.findPostsOrderByVoteCount(PostType.VOTE, PostState.POSTING, minusDays, user.getId(), pageable);

        // 랜덤으로 섞기
        List<Post> postList = new ArrayList<>(recentPostPage.getContent());
        // 리스트를 랜덤하게 섞기
        Collections.shuffle(postList);
        // 섞인 리스트를 페이지로 다시 변환
        Page<Post> shuffledPage = new PageImpl<>(postList, recentPostPage.getPageable(), postList.size());

        return PostConverter.pollPostGetListDTO(shuffledPage, userId);
    }

    @Override
    public Post_scrap createScrapPost(Long postId, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            this.checkPost(false);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Post post = optionalPost.get();
        User user = optionalUser.get();

        Post_scrap post_scrap = PostConverter.toPostScrap(post, user);
        Optional<Post_scrap> optionalPost_scrap = postScrapRepository.findByPostIdAndUserId(postId, userId);
        if (!optionalPost_scrap.isEmpty()) {
            this.checkPostScrap(true);
        }
        return postScrapRepository.save(post_scrap);
    }

    @Override
    public void deleteScrapPost(Long postId, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            this.checkPost(false);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Optional<Post_scrap> optionalPost_scrap = postScrapRepository.findByPostIdAndUserId(postId, userId);
        if (optionalPost_scrap.isEmpty()) {
            this.checkPostScrap(false);
        }

        Post_scrap post_scrap = optionalPost_scrap.get();
        postScrapRepository.delete(post_scrap);
    }

}
