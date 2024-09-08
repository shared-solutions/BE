package friend.spring.repository;

import friend.spring.domain.Card_vote;
import friend.spring.domain.Gauge_poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Card_VoteRepository extends JpaRepository<Card_vote, Long> {
    List<Card_vote> findByUserId(Long user_id);
}
