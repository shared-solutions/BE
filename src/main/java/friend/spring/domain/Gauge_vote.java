package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Gauge_vote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gauge_poll_id")
    private Gauge_poll gaugePoll;


    public void setGaugePoll(Gauge_poll gaugePoll){
        if(this.gaugePoll != null)
            gaugePoll.getGaugeVoteList().remove(this);
        this.gaugePoll = gaugePoll;
        gaugePoll.getGaugeVoteList().add(this);
    }
    public void setUser(User user){
        if(this.user != null)
            user.getGaugeVoteList().remove(this);
        this.user = user;
        user.getGaugeVoteList().add(this);
    }
}
