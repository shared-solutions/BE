package friend.spring.repository;

import friend.spring.domain.mapping.Comment_like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<Comment_like, Long> {
    Optional<Comment_like> findByCommentIdAndUserId(Long commentId, Long userId);
}
