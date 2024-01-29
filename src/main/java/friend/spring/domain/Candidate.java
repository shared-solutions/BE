package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Candidate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "generalPoll_id")
//    private General_poll generalPoll;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "generalPoll_id")
    private General_poll generalPoll;



//    public void setGeneralPoll(General_poll generalPoll){
//        this.generalPoll = generalPoll;
//        if (generalPoll != null) {
//            generalPoll.getCandidateList().add(this);
//        }
//
//    }
    public void setGeneralPoll(General_poll generalPoll) {
    this.generalPoll = generalPoll;
    if (this.generalPoll != null) {
        Hibernate.initialize(this.generalPoll);
        if (this.generalPoll.getCandidateList() != null) {
            this.generalPoll.getCandidateList().add(this);
        }
    }
}


}
