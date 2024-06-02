package friend.spring.repository;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import friend.spring.domain.User;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPostTypeAndState(PostType postType, PostState state, Pageable pageable);

    Page<Post> findByPostTypeAndStateAndCategory(PostType postType, PostState state, Category category, Pageable pageable);

    Page<Post> findByUserIdAndPostTypeAndState(Long userId, PostType postType, PostState state, Pageable pageable);

    Page<Post> findAllByUser(User user, PageRequest pageRequest);

    Page<Post> findByPostTypeAndStateAndCreatedAtAfter(PostType postType, PostState state, LocalDateTime sevenDaysAgo, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN p.postScrapList s JOIN p.category c WHERE s.user.id = :userId and c.id = :categoryId")
    Page<Post> findCategoryDetail(Long userId, Long categoryId, PageRequest pageRequest);
}
