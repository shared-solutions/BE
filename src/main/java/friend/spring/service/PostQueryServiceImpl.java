package friend.spring.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

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
    public Optional<Post> getPostDetail(Long postId){
        Optional<Post> postOptional=postRepository.findById(postId);
        Post post = postOptional.get();
        post.setView(post.getView()+1);
        return postOptional;
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
                if (!vote.isEmpty()) { // 목록이 비어 있지 않으면 true를 반환
                    return true;
                }
            }
        }
        if(post.getPostType()== PostType.VOTE){
            if(post.getVoteType()== PostVoteType.CARD){
                List<Card_vote> vote=cardVoteRepository.findByUserId(userId);
                if (!vote.isEmpty()) { // 목록이 비어 있지 않으면 true를 반환
                    return true;
                }
            }
        }
        if(post.getPostType()== PostType.VOTE){
            if(post.getVoteType()== PostVoteType.GAUGE){
                List<Gauge_vote> vote=gaugeVoteRepository.findByUserId(userId);
                if (!vote.isEmpty()) { // 목록이 비어 있지 않으면 true를 반환
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
        return postRepository.findByUserIdAndPostTypeAndState(userId, PostType.VOTE, PostState.POSTING, pageable);
    }

}
