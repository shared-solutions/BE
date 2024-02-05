package friend.spring.repository;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import org.springframework.data.domain.Page;
import friend.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPostTypeAndState(PostType postType, PostState state, Pageable pageable);
    Page<Post> findByPostTypeAndStateAndCategory(PostType postType, PostState state,Category category,Pageable pageable);
    Page<Post> findByUserIdAndPostTypeAndState(Long userId, PostType postType, PostState state, Pageable pageable);
    Page<Post> findAllByUser(User user, PageRequest pageRequest);
    @Query(value = "SELECT * FROM post WHERE post.created_at >= DATE_ADD(now(), INTERVAL -24 HOUR) ORDER BY post.point DESC", nativeQuery = true)
    Page<Post> findBestPosts(Pageable pageable);
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
