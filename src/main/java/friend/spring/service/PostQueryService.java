package friend.spring.service;

import friend.spring.domain.Post;

import java.util.Optional;

public interface PostQueryService {
    Optional<Post> getPostDetail(Long postId);
    Boolean checkEngage(Long userId, Long postId);

    Post ParentPost(Long parentid);
}
