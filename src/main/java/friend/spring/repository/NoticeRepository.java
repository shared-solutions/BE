package friend.spring.repository;

import friend.spring.domain.Notice;
import friend.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByUser(User user, PageRequest pageRequest);
}
