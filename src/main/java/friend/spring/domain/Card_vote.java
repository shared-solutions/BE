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
public class Card_vote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @Builder.Default
    @Column(nullable = true, length = 100)
    private List<Long> select_list=new ArrayList<Long>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_poll_id")
    private Card_poll cardPoll;

    public void setSelect_list(List<Long> select_list) {
        if (select_list != null) {
            this.select_list = new ArrayList<Long>(select_list);
        } else {
            this.select_list.clear();
        }
    }
    public void setCardPoll(Card_poll cardPoll){
        if(this.cardPoll != null)
            cardPoll.getCardVoteList().remove(this);
        this.cardPoll = cardPoll;
        cardPoll.getCardVoteList().add(this);
    }
    public void setUser(User user){
        if(this.user != null)
            user.getCardVoteList().remove(this);
        this.user = user;
        user.getCardVoteList().add(this);
    }
}
