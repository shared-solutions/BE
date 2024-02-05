package friend.spring.service;

import friend.spring.domain.Post;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostQueryService {
    Optional<Post> getPostDetail(Long postId);
    Boolean checkEngage(Long userId, Long postId);

    Post ParentPost(Long parentid);

    Optional<Post> findPost(Long postId);

    Page<Post> getPostList(Integer page,Integer size, String category);

    Page<Post> getReviewList(Integer page, Integer size, Integer arrange);

    Page<Post> getParentPostList(Integer page,Integer size,Long userId);
}
