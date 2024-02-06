package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.PostHandler;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.converter.CandidateConverter;
import friend.spring.converter.PostConverter;
import friend.spring.domain.*;
import friend.spring.domain.mapping.Post_like;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.repository.*;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static friend.spring.domain.enums.PostType.*;
import static friend.spring.domain.enums.PostVoteType.*;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

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

    @Override
    public void checkPost(Boolean flag) {
        if (!flag) {
            throw new PostHandler(ErrorStatus.POST_NOT_FOUND);
        }
    }

    @Override
    public void checkPostWriterUser(Boolean flag) {
        if (!flag) {
            throw new PostHandler(ErrorStatus.POST_NOT_CORRECT_USER);
        }
    }

    @Override
    public Boolean checkPoint(PostRequestDTO.AddPostDTO request, User user) {
        if(request.getPoint()>user.getPoint()){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void checkPostLike(Boolean flag) {
        if (!flag) {
            throw new PostHandler(ErrorStatus.POST_LIKE_NOT_FOUND);
        }
    }

    @Override
    public void checkPostScrap(Boolean flag) {
        if (!flag) {
            throw new PostHandler(ErrorStatus.POST_SCRAP_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public Post joinPost(PostRequestDTO.AddPostDTO request, HttpServletRequest request2) {

        Long userId = jwtTokenProvider.getCurrentUser(request2);
        Post newPost= PostConverter.toPost(request);
        User user=userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("\""+userId+"\"해당 유저가 없습니다"));
        newPost.setUser(user);

        //일반 투표 api
        if(newPost.getPostType()==VOTE){
            newPost.setCategory(categoryRepository.findByName(request.getCategory()));

        }
        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==GENERAL&&request.getPollOption()!=null){
            //포인트 차감 관련 코드
            if(request.getPoint()!=null) {
                if (!checkPoint(request, user)) {
                    throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
                }
                user.setPoint(user.getPoint() - request.getPoint());
                Point newPoint=Point.builder()
                        .amount(user.getPoint())
                        .content("일반 투표 작성에 대한 "+request.getPoint()+" 포인트 차감")
                        .build();
                newPoint.setUser(user);
                pointRepository.save(newPoint);
            }

            General_poll generalPoll = General_poll.builder()
                    .pollTitle(request.getPollTitle())
                    .deadline(request.getDeadline())
                    .build();
            if(request.getMultipleChoice()!=null){
                generalPoll.setMultipleChoice(request.getMultipleChoice());
            }
            newPost.setGeneralPoll(generalPoll);
            generalPollRepository.save(generalPoll);


            for (PollOptionDTO option : request.getPollOption()) {
                Candidate candidate = Candidate.builder()
                        .name(option.getOptionString())
                        .image(option.getOptionImg())
                        .build();

                candidate.setGeneralPoll(generalPoll);
                candidateRepository.save(candidate);
            }
        }
//카드 투표 api
        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==CARD&&request.getPollOption()!=null){
            //포인트 차감 관련 코드
            if(request.getPoint()!=null) {
                if (!checkPoint(request, user)) {
                    throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
                }
                user.setPoint(user.getPoint() - request.getPoint());
                Point newPoint=Point.builder()
                        .amount(user.getPoint())
                        .content("게이지 투표 등록에 대한 "+request.getPoint()+" 포인트 차감")
                        .build();
                newPoint.setUser(user);
                pointRepository.save(newPoint);
            }

            if(!checkPoint(request, user)&&request.getPoint()!=null){
                throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
            }

            Card_poll cardPoll = Card_poll.builder()
                    .pollTitle(request.getPollTitle())
                    .deadline(request.getDeadline())
                    .build();
            if(request.getMultipleChoice()!=null){
                cardPoll.setMultipleChoice(request.getMultipleChoice());
            }
            newPost.setCardPoll(cardPoll);
            cardPollRepository.save(cardPoll);

            for (PollOptionDTO option : request.getPollOption()) {
                Candidate candidate = Candidate.builder()
                        .name(option.getOptionString())
                        .image(option.getOptionImg())
                        .build();

                candidate.setCardPoll(cardPoll);
                candidateRepository.save(candidate);
            }
        }
//게이지 투표 api
        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==GAUGE){
            //포인트 차감 관련 코드
            if(request.getPoint()!=null) {
                if (!checkPoint(request, user)) {
                    throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
                }
                user.setPoint(user.getPoint() - request.getPoint());
                Point newPoint=Point.builder()
                        .amount(user.getPoint())
                        .content("카드 투표 등록에 대한 "+request.getPoint()+" 포인트 차감")
                        .build();
                newPoint.setUser(user);
                pointRepository.save(newPoint);
            }

            if(!checkPoint(request, user)&&request.getPoint()!=null){
                throw new RuntimeException("\""+userId+"\"해당 유저의 포인트가 부족 합니다");
            }

            Gauge_poll gaugePoll = Gauge_poll.builder()
                    .pollTitle(request.getPollTitle())
                    .gauge(0)
                    .deadline(request.getDeadline())
                    .build();

            newPost.setGaugePoll(gaugePoll);
            gaugePollRepository.save(gaugePoll);
        }

        if(newPost.getPostType()==REVIEW&&request.getParent_id()!=null){
            Post parent=postRepository.findById(request.getParent_id())
                            .orElseThrow(()->new RuntimeException("\""+request.getParent_id()+"\"해당 글이 없습니다"));
            if(!userId.equals(parent.getUser().getId())){
                throw new RuntimeException("타 유저 글입니다. 후기글 작성 권한이 없습니다.");
            }
            newPost.setParentPost(parent);
        }

        return postRepository.save(newPost);

    }

    @Override
    @Transactional
    public void editPost(Long postId,PostRequestDTO.PostEditReq request, Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post=postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        if(!user.getId().equals(post.getUser().getId())){
            throw new RuntimeException("수정 권환이 없습니다 글이 없습니다");
        }
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post=postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        if(!user.getId().equals(post.getUser().getId())){
            throw new RuntimeException("삭제 권환이 없습니다 글이 없습니다");
        }
        post.setStateDel();
    }


    //한 유저의 모든 질문글
    @Override
    public Page<Post> getMyPostList(Long userId, Integer page) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
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
    public Page<PostResponseDTO.PostSummaryListRes> getBestPosts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> bestPostPage = postRepository.findBestPosts(pageable);
        List<PostResponseDTO.PostSummaryListRes> postResList = getPostRes(bestPostPage);
        return new PageImpl<>(postResList, pageable, bestPostPage.getTotalElements());
    }

    @Override
    public Page<PostResponseDTO.PostSummaryListRes> getRecentPosts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> recentPostPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<PostResponseDTO.PostSummaryListRes> postResList = getPostRes(recentPostPage);
        return new PageImpl<>(postResList, pageable, recentPostPage.getTotalElements());
    }

    @Override
    public List<PostResponseDTO.PostSummaryListRes> getPostRes(Page<Post> postPage) {
        return postPage
                .map(post -> {
                    Integer like_cnt = postLikeRepository.countByPostId(post.getId());
                    Integer comment_cnt = commentRepository.countByPostId(post.getId());
                    String postVoteType = null;
                    Long general_poll_id = null;
                    Long gauge_poll_id = null;
                    Long card_poll_id = null;
                    List<Candidate> candidateList = null;
                    List<CandidateResponseDTO.CandidateSummaryRes> candidateSummaryResList = null;
                    if (post.getVoteType().equals(GENERAL)) { // 일반 투표인 경우
                        postVoteType = "GENERAL";
                        general_poll_id = post.getGeneralPoll().getId();
                        candidateList = candidateRepository.findAllByGeneralPollId(general_poll_id);
                        candidateSummaryResList = new ArrayList<>();

                        // 총 투표수 계산
                        Double totalVotes = (double) post.getGeneralPoll().getGeneralVoteList().stream()
                                .flatMap(vote -> vote.getSelect_list().stream())
                                .count();

                        // 각 후보별 선택된 횟수 계산
                        Map<Long, Long> candidateSelectionCounts = post.getGeneralPoll().getGeneralVoteList().stream()
                                .flatMap(vote -> vote.getSelect_list().stream())
                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                        for (Candidate candidate : candidateList) {
                            long selectionCount = candidateSelectionCounts.getOrDefault(candidate.getId(), 0L);
                            Double percent = (double) selectionCount / totalVotes * 100;
                            candidateSummaryResList.add(CandidateConverter.toCandidateSummaryRes(candidate, percent));
                        }
                    } else if (post.getVoteType().equals(GAUGE)) {
                        postVoteType = "GAUGE";
                        gauge_poll_id = post.getGaugePoll().getId();
                    } else {
                        postVoteType = "CARD";
                        card_poll_id = post.getCardPoll().getId();
                        candidateList = candidateRepository.findAllByCardPollId(card_poll_id);
                        candidateSummaryResList = new ArrayList<>();

                        // 총 투표수 계산
                        Double totalVotes = (double) post.getCardPoll().getCardVoteList().stream()
                                .flatMap(vote -> vote.getSelect_list().stream())
                                .count();

                        // 각 후보별 선택된 횟수 계산
                        Map<Long, Long> candidateSelectionCounts = post.getCardPoll().getCardVoteList().stream()
                                .flatMap(vote -> vote.getSelect_list().stream())
                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                        for (Candidate candidate : candidateList) {
                            long selectionCount = candidateSelectionCounts.getOrDefault(candidate.getId(), 0L);
                            Double percent = (double) selectionCount / totalVotes * 100;
                            candidateSummaryResList.add(CandidateConverter.toCandidateSummaryRes(candidate, percent));
                        }
                    }

                    PostResponseDTO.PostSummaryListRes postGetRes = PostConverter.toPostSummaryRes(post, like_cnt, comment_cnt, postVoteType, candidateSummaryResList, general_poll_id, gauge_poll_id, card_poll_id);
                    return postGetRes;
                })
                .filter(Objects::nonNull) // null인 요소는 필터링
                .get()
                .collect(Collectors.toList());
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
