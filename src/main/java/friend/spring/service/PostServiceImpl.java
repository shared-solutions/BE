package friend.spring.service;

import friend.spring.converter.General_voteConverter;
import friend.spring.converter.PostConverter;
import friend.spring.domain.General_vote;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.repository.General_voteRepository;
import friend.spring.repository.PostRepository;
import friend.spring.repository.UserRepository;
import friend.spring.web.dto.PostRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static friend.spring.domain.enums.PostType.VOTE;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final General_voteRepository generalVoteRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Post joinPost(PostRequestDTO.AddPostDTO request, Long userId) {
        Post newPost= PostConverter.toPost(request);
        User user=userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("\""+userId+"\"해당 유저가 없습니다"));
        newPost.setUser(user);

        if(newPost.getPostType()==VOTE&&request.getPollOption()!=null){
            General_vote generalVote = General_voteConverter.toGeneralVoteList(request.getPollOption());
            newPost.setGeneralVote(generalVote);

            generalVoteRepository.save(generalVote);

        }

        return postRepository.save(newPost);
    }
}
