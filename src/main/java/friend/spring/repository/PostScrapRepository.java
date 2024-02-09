package friend.spring.repository;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Post_scrap;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostScrapRepository extends JpaRepository<Post_scrap, Long> {

    Optional<Post_scrap> findByPostIdAndUserId(Long postId, Long userId);

    List<Post_scrap> findAllByUser(User user);

    // 사용자가 스크랩한 모든 글을 최신순으로 정렬하여 페이징하여 조회
    @Query("SELECT p FROM Post p JOIN p.postScrapList s WHERE s.user.id = :userId ORDER BY p.createdAt DESC")
    Page<Post> findPostsByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // 사용자가 스크랩한 모든 글을 조회수 순으로 정렬하여 페이징하여 조회
    @Query("SELECT p FROM Post p JOIN p.postScrapList s WHERE s.user.id = :userId ORDER BY p.view DESC")
    Page<Post> findPostsByUserIdOrderByPostViewDesc(Long userId, Pageable pageable);
}
