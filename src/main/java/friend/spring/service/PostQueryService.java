package friend.spring.service;

import friend.spring.domain.Post;

import java.util.Optional;

public interface PostQueryService {
    Optional<Post> getPostDetail(Long postId);
}
