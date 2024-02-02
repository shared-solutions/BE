package friend.spring.service;

import friend.spring.converter.PostConverter;
import friend.spring.domain.*;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.repository.*;
import friend.spring.web.dto.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
}
