package friend.spring.repository;

import friend.spring.domain.Post;
import friend.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByMyPost(User user, PageRequest pageRequest);
}
