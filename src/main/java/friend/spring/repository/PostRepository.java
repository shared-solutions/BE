package friend.spring.repository;

import friend.spring.domain.Post;
import friend.spring.domain.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPostType(PostType postType, Pageable pageable);
    Page<Post> findByUserIdAndPostType(Long userId, PostType postType, Pageable pageable);
}
