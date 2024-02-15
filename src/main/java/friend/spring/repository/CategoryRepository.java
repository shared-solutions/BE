package friend.spring.repository;

import friend.spring.domain.Card_vote;
import friend.spring.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String category);
}
