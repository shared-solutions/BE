package friend.spring.repository;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import friend.spring.domain.User;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPostTypeAndState(PostType postType, PostState state, Pageable pageable);

    Page<Post> findByPostTypeAndStateAndCategory(PostType postType, PostState state, Category category, Pageable pageable);

    Page<Post> findByUserIdAndPostTypeAndState(Long userId, PostType postType, PostState state, Pageable pageable);

    Page<Post> findAllByUser(User user, PageRequest pageRequest);

    Page<Post> findByPostTypeAndStateAndCreatedAtAfter(PostType postType, PostState state, LocalDateTime sevenDaysAgo, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN p.postScrapList s JOIN p.category c WHERE s.user.id = :userId and c.id = :categoryId")
    Page<Post> findCategoryDetail(Long userId, Long categoryId, PageRequest pageRequest);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN General_poll gp ON p.generalPoll.id = gp.id " +
            "LEFT JOIN Gauge_poll ggp ON p.gaugePoll.id = ggp.id " +
            "LEFT JOIN Card_poll cp ON p.cardPoll.id = cp.id " +
            "WHERE p.postType = :vote " +
            "AND p.state = :posting " +
            "AND p.createdAt >= :minusDays " +
            // 투표 마감 안 된 글들만 필터링
            "AND (" +
            "   (gp.deadline >= CURRENT_TIMESTAMP AND ggp.deadline IS NULL AND cp.deadline IS NULL) OR " +
            "   (gp.deadline IS NULL AND ggp.deadline >= CURRENT_TIMESTAMP AND cp.deadline IS NULL) OR " +
            "   (gp.deadline IS NULL AND ggp.deadline IS NULL AND cp.deadline >= CURRENT_TIMESTAMP)" +
            ") " +
            // 로그인한 사용자가 투표 안 한 글들만 필터링
            "AND (" +
            "   gp.id NOT IN (SELECT gv.generalPoll.id FROM General_vote gv WHERE gv.user.id = :userId) OR " +
            "   ggp.id NOT IN (SELECT gv.gaugePoll.id FROM Gauge_vote gv WHERE gv.user.id = :userId) OR " +
            "   cp.id NOT IN (SELECT gv.cardPoll.id FROM Card_vote gv WHERE gv.user.id = :userId)" +
            ") " +
            //각 투표 유형에 대한 투표 수를 합산해(어차피 하나에만 값이 있음) 투표 수가 적은 순으로 정렬
            "ORDER BY (" +
            "   (SELECT COUNT(gv.id) FROM General_vote gv WHERE gv.generalPoll.id = gp.id) + " +
            "   (SELECT COUNT(gv.id) FROM Gauge_vote gv WHERE gv.gaugePoll.id = ggp.id) + " +
            "   (SELECT COUNT(gv.id) FROM Card_vote gv WHERE gv.cardPoll.id = cp.id)" +
            ") ASC")
    Page<Post> findPostsOrderByVoteCount(@Param("vote") PostType vote,
                                         @Param("posting") PostState posting,
                                         @Param("minusDays") LocalDateTime minusDays,
                                         @Param("userId") Long userId,
                                         Pageable pageable);
}
