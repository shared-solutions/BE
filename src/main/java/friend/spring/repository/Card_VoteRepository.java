package friend.spring.repository;

import friend.spring.domain.Card_vote;
import friend.spring.domain.Gauge_poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Card_VoteRepository extends JpaRepository<Card_vote, Long> {
}
