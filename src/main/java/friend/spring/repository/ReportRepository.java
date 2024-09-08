package friend.spring.repository;

import friend.spring.domain.Report;
import friend.spring.domain.enums.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByTargetTypeAndTargetIdAndUserId(ReportType post, Long postId, Long userId);
}
