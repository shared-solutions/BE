package friend.spring.repository;

import friend.spring.domain.Comment;
import friend.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndParentCommentIsNull(Long postId);

    Page<Comment> findAllByUser(User user, PageRequest pageRequest);

    Integer countByPostId(Long postId);
}
