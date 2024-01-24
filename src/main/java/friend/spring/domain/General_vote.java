package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class General_vote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 투표 옵션을 저장하는 리스트
    @ElementCollection
    @CollectionTable(name = "general_vote_options", joinColumns = @JoinColumn(name = "general_vote_id"))
    @Column(name = "option")
    private List<String> options = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_question_id")
    private General_question generalQuestion;

    @OneToOne(mappedBy = "generalVote", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Post post;

    public void setPost(Post post) {
        this.post = post;
        if (post != null) {
            post.setGeneralVote(this);
        }
    }
}
