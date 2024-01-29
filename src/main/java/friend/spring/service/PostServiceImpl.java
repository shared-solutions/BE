package friend.spring.service;

import friend.spring.converter.PostConverter;
import friend.spring.domain.Candidate;
import friend.spring.domain.General_poll;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.repository.CandidateRepository;
import friend.spring.repository.General_PollRepository;
import friend.spring.repository.PostRepository;
import friend.spring.repository.UserRepository;
import friend.spring.web.dto.PollOptionDTO;
import friend.spring.web.dto.PostRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static friend.spring.domain.enums.PostType.*;
import static friend.spring.domain.enums.PostVoteType.GAUGE;
import static friend.spring.domain.enums.PostVoteType.GENERAL;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final General_PollRepository generalPollRepository;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;

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

        if(newPost.getPostType()==VOTE&&newPost.getVoteType()==GAUGE&&request.getPollOption()!=null){
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

        if(newPost.getPostType()==REVIEW&&request.getParent_id()!=null){
            Post parent=postRepository.findById(request.getParent_id())
                            .orElseThrow(()->new RuntimeException("\""+request.getParent_id()+"\"해당 글이 없습니다"));
            newPost.setParentPost(parent);
        }

        return postRepository.save(newPost);
    }
}
