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

    @Column(nullable = false)
    private Integer min=0;

    @Column(nullable = false)
    private Integer max=100;

    @Column(nullable = false)
    private Integer gauge =0;

    @Column(nullable = true)
    private Timestamp deadline= Timestamp.valueOf(LocalDateTime.now().plusHours(1)); // 디폴트 시간 1시간 설정.

    @OneToOne(mappedBy = "gaugePoll", cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder.Default
    @OneToMany(mappedBy = "gaugePoll")
    private List<Gauge_vote> gaugeVoteList = new ArrayList<>();

    public void setPost(Post post) {
        this.post = post;
    }
}
