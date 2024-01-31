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
public class Card_poll extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Timestamp deadline= Timestamp.valueOf(LocalDateTime.now().plusHours(1)); // 디폴트 시간 1시간 설정.

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @OneToMany(mappedBy = "cardPoll", cascade = CascadeType.ALL)
    private List<Card_vote> cardVoteList = new ArrayList<>();

    @OneToOne(mappedBy = "cardPoll", cascade = CascadeType.ALL)
    private Post post;

    @OneToMany(mappedBy = "cardPoll",cascade = CascadeType.ALL)
    private List<Candidate> candidateList=new ArrayList<>();

    public void setPost(Post post) {
        this.post = post;
    }
}
