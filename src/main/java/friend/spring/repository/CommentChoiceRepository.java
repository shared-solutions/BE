package friend.spring.repository;

import friend.spring.domain.mapping.Comment_choice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentChoiceRepository extends JpaRepository<Comment_choice, Long> {
    Optional<Comment_choice> findByPostId(Long postId);

    Optional<Comment_choice> findByCommentIdAndPostId(Long commentId, Long postId);
}
