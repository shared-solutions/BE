package friend.spring.repository;

import friend.spring.domain.General_vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface General_VoteRepository  extends JpaRepository<General_vote, Long> {
}
