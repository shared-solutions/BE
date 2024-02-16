package friend.spring.repository;

import friend.spring.domain.Alarm;
import friend.spring.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findAllByUser(User user, PageRequest pageRequest);

    Boolean existsByUserIdAndReadIsFalse(Long userId);
}
