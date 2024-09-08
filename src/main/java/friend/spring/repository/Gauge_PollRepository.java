package friend.spring.repository;

import friend.spring.domain.Gauge_poll;
import friend.spring.domain.Gauge_vote;
import friend.spring.domain.General_vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Gauge_PollRepository extends JpaRepository<Gauge_poll, Long> {
}
