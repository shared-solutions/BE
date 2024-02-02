package friend.spring.service;

import friend.spring.converter.PostConverter;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.repository.PostRepository;
import friend.spring.repository.UserRepository;
import friend.spring.web.dto.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public Optional<Post> getPostDetail(Long postId){
        Optional<Post> postOptional=postRepository.findById(postId);
        Post post = postOptional.get();
        post.setView(post.getView()+1);
        return postOptional;
    }

    @Override
    public Boolean checkEngage(Long userId,Long postId){
        Boolean engage = postRepository.f
        return postOptional;
    }
}
