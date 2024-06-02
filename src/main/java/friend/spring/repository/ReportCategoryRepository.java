package friend.spring.repository;

import friend.spring.domain.Category;
import friend.spring.domain.Report;
import friend.spring.domain.ReportCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportCategoryRepository extends JpaRepository<ReportCategory, Long> {
    ReportCategory findByName(String category);
}
