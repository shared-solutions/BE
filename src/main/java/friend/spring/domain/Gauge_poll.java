package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Gauge_poll extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String pollTitle;

    @Builder.Default
    @Column(nullable = false)
    private Integer gauge = 0;

    @Builder.Default
    @Column(nullable = true)
    private LocalDateTime deadline = LocalDateTime.now().plusHours(1); // 디폴트 시간 1시간 설정.

    @Column(nullable = false)
    @Builder.Default
    private Boolean VoteOnGoing = true;

    @OneToOne(mappedBy = "gaugePoll", cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder.Default
    @OneToMany(mappedBy = "gaugePoll")
    private List<Gauge_vote> gaugeVoteList = new ArrayList<>();

    public void setPost(Post post) {
        this.post = post;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setVoteOnGoing(Boolean voteOnGoing) {
        this.VoteOnGoing = voteOnGoing;
    }

    public void setGauge(Integer gauge) {
        this.gauge = gauge;
    }
}
