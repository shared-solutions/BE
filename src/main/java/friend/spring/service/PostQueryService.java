package friend.spring.service;

import friend.spring.domain.Post;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostQueryService {
    Optional<Post> getPostDetail(Long postId);
    Boolean checkEngage(Long userId, Long postId);

    Post ParentPost(Long parentid);

    Optional<Post> findPost(Long postId);

    Page<Post> getPostList(Integer page,Integer size);
}
