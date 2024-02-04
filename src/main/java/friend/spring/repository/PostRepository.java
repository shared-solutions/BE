package friend.spring.repository;

import friend.spring.domain.Post;
import friend.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser(User user, PageRequest pageRequest);

    @Query(value = "SELECT * FROM post WHERE post.created_at >= DATE_ADD(now(), INTERVAL -24 HOUR) ORDER BY post.point DESC", nativeQuery = true)
    Page<Post> findBestPosts(Pageable pageable);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
