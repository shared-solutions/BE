package friend.spring.repository;

import friend.spring.domain.Gauge_poll;
import friend.spring.domain.Gauge_vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Gauge_VoteRepository extends JpaRepository<Gauge_vote, Long> {
    List<Gauge_vote> findByUserId(Long user_id);
}
