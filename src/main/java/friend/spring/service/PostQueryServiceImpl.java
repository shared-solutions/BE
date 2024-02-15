package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.converter.PostConverter;
import friend.spring.domain.*;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.repository.*;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final General_VoteRepository generalVoteRepository;
    private final Gauge_VoteRepository gaugeVoteRepository;
    private final Card_VoteRepository cardVoteRepository;
    private final CategoryRepository categoryRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    @Transactional
    public Post getPostDetail(Long postId){
        Post post=postRepository.findById(postId).orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        post.setView(post.getView()+1);
        return post;
    }

    @Override
    public Boolean checkEngage(Long postId,Long userId){
        Optional<Post> postOptional=postRepository.findById(postId);
        Post post = postOptional.get();
        Boolean engage;
        if(post.getUser().getId().equals(userId)){
            return true;
        }
        if(post.getPostType()== PostType.VOTE){
            if(post.getVoteType()== PostVoteType.GENERAL){
                List<General_vote> vote=generalVoteRepository.findByUserId(userId);
                Boolean isIn=vote.stream().filter(pollPost->pollPost.getGeneralPoll()==post.getGeneralPoll()).collect(Collectors.toList()).isEmpty();
                if (!isIn) { // 목록이 비어 있지 않으면 true를 반환
                    return true;
                }
            }
        }
        if(post.getPostType()== PostType.VOTE){
            if(post.getVoteType()== PostVoteType.CARD){
                List<Card_vote> vote=cardVoteRepository.findByUserId(userId);
                Boolean isIn=vote.stream().filter(pollPost->pollPost.getCardPoll()==post.getCardPoll()).collect(Collectors.toList()).isEmpty();
                if (!isIn) { // 목록이 비어 있지 않으면 true를 반환
                    return true;
                }
            }
        }
        if(post.getPostType()== PostType.VOTE){
            if(post.getVoteType()== PostVoteType.GAUGE){
                List<Gauge_vote> vote=gaugeVoteRepository.findByUserId(userId);
                Boolean isIn=vote.stream().filter(pollPost->pollPost.getGaugePoll()==post.getGaugePoll()).collect(Collectors.toList()).isEmpty();
                if (!isIn) { // 목록이 비어 있지 않으면 true를 반환
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    public Post ParentPost(Long parentid){
        Optional<Post> parentPostOptional=postRepository.findById(parentid);
        return parentPostOptional.get().getParentPost();
    }

    @Override
    public Optional<Post> findPost(Long postId) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public Page<Post> getPostList(Integer page,Integer size,String category){
        if(category.equals("모두")){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findByPostTypeAndState(PostType.VOTE, PostState.POSTING, pageable);}
        Category category1 = categoryRepository.findByName(category);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findByPostTypeAndStateAndCategory(PostType.VOTE, PostState.POSTING,category1, pageable);
    }

    @Override
    @Transactional
    public Page<Post> getReviewList(Integer page, Integer size, Integer arrange){
        if(arrange==1){
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            return postRepository.findByPostTypeAndState(PostType.REVIEW, PostState.POSTING, pageable);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "view"));
        return postRepository.findByPostTypeAndState(PostType.REVIEW, PostState.POSTING, pageable);
    }

    @Override
    @Transactional
    public Page<Post> getParentPostList(Integer page, Integer size, HttpServletRequest request){
        Long userId = jwtTokenProvider.getCurrentUser(request);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        List<Post> userPost = postRepository.findByUserId(userId);
        Page<Post> allPost= postRepository.findByUserIdAndPostTypeAndState(userId, PostType.VOTE, PostState.POSTING, pageable);
        List<Post> filterEndPost=allPost.getContent().stream()
                .filter(post->{
                    LocalDateTime deadline=null;
                    switch (post.getVoteType()){
                        case GENERAL:
                            deadline=post.getGeneralPoll().getDeadline();
                            break;
                        case GAUGE:
                            deadline=post.getGaugePoll().getDeadline();
                            break;
                        case CARD:
                            deadline=post.getCardPoll().getDeadline();
                            break;
                    }
                    return deadline !=null&&deadline.isBefore(LocalDateTime.now());
                }).collect(Collectors.toList());
        return new PageImpl<>(filterEndPost,pageable, filterEndPost.size());

    }

}
