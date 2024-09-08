package friend.spring.service;

import friend.spring.domain.Post;
import friend.spring.domain.Redis.SearchLog;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface PostQueryService {
    Post getPostDetail(Long postId);

    Boolean checkEngage(Long userId, Long postId);

    Post ParentPost(Long parentid);

    Optional<Post> findPost(Long postId);

    Page<Post> getPostList(Integer page, Integer size, String category);

    Page<Post> getReviewList(Integer page, Integer size, Integer arrange);

    Page<Post> getParentPostList(Integer page, Integer size, HttpServletRequest request);
    Page<Post> getPostSearch(Long userId,Integer page, Integer size, String search);
    List<String> getRecentSearchLogs(Long userId);

}
