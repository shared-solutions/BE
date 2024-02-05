package friend.spring.repository;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPostTypeAndState(PostType postType, PostState state, Pageable pageable);
    Page<Post> findByPostTypeAndStateAndCategory(PostType postType, PostState state,Category category,Pageable pageable);
    Page<Post> findByUserIdAndPostTypeAndState(Long userId, PostType postType, PostState state, Pageable pageable);
}
