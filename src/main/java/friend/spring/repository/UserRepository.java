package friend.spring.repository;

import friend.spring.domain.User;
import friend.spring.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
//    Optional<User> findByEmailAndRole(String email, RoleType roleType);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);
}
