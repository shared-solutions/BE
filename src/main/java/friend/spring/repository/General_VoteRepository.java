package friend.spring.repository;

import friend.spring.domain.General_vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface General_VoteRepository  extends JpaRepository<General_vote, Long> {
    List<General_vote> findByUserId(Long user_id);
}
