package friend.spring.service;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.PostHandler;
import friend.spring.apiPayload.handler.UserHandler;
import friend.spring.converter.PostConverter;
import friend.spring.domain.*;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.repository.*;
import friend.spring.web.dto.PollOptionDTO;
import friend.spring.web.dto.PostRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Post joinPost(PostRequestDTO.AddPostDTO request, Long userId) {

        
        Post newPost= PostConverter.toPost(request);
        User user=userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("\""+userId+"\"해당 유저가 없습니다"));
        newPost.setUser(user);
        if(request.getTag()!=null){
            newPost.setTags(request.getTag());
        }

//        if(newPost.getPostType()==NOT_VOTE){
//
//        }

        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==GENERAL&&request.getPollOption()!=null){
            General_poll generalPoll = General_poll.builder()
                    .deadline(request.getDeadline())
                    .build();
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

        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==CARD&&request.getPollOption()!=null){
            Card_poll cardPoll = Card_poll.builder()
                    .deadline(request.getDeadline())
                    .build();
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

        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==GAUGE&&request.getPollOption()!=null){
            Gauge_poll gaugePoll = Gauge_poll.builder()
                    .max(100)
                    .min(0)
                    .gauge(0)
                    .deadline(request.getDeadline())
                    .build();

            newPost.setGaugePoll(gaugePoll);
            gaugePollRepository.save(gaugePoll);
        }

        if(newPost.getPostType()==REVIEW&&request.getParent_id()!=null){
            Post parent=postRepository.findById(request.getParent_id())
                            .orElseThrow(()->new RuntimeException("\""+request.getParent_id()+"\"해당 글이 없습니다"));
            newPost.setParentPost(parent);
        }

        return postRepository.save(newPost);

    }
}
