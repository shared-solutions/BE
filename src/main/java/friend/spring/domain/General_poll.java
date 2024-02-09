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
public class General_poll extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String pollTitle;

    @Column(nullable = true)
    @Builder.Default
    private Boolean multipleChoice=true;

    @Column(nullable = true)
    @Builder.Default
    private LocalDateTime deadline= LocalDateTime.now().plusHours(1); // 디폴트 시간 1시간 설정.

    @Column(nullable = true)
    @Builder.Default
    private Boolean VoteOnGoing=true;

    @Builder.Default
    @OneToMany(mappedBy = "generalPoll", cascade = CascadeType.ALL)
    private List<General_vote> generalVoteList = new ArrayList<>();

    @OneToOne(mappedBy = "generalPoll", cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder.Default
    @OneToMany(mappedBy = "generalPoll",cascade = CascadeType.ALL)
    private List<Candidate> candidateList=new ArrayList<>();

    public void setPost(Post post) {
        this.post = post;
    }

    public void setDeadline(LocalDateTime deadline){
        this.deadline=deadline;
    }
    public void setVoteOnGoing(Boolean voteOnGoing){
        this.VoteOnGoing=voteOnGoing;
    }
    public void setMultipleChoice(Boolean multipleChoice){
        this.multipleChoice=multipleChoice;
    }

}
