package friend.spring.repository;

import friend.spring.domain.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findAllByGeneralPollId(Long general_poll_id);

    List<Candidate> findAllByCardPollId(Long card_poll_id);
}
