package friend.spring.repository;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Post_scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostScrapRepository extends JpaRepository<Post_scrap, Long> {
    Page<Post_scrap> findAllByUser(User user);
}
