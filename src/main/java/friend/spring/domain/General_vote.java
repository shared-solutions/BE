package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class General_vote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean select1;

    @Column(nullable = true)
    private Boolean select2;

    @Column(nullable = true)
    private Boolean select3;

    @Column(nullable = true)
    private Boolean select4;

    @Column(nullable = true)
    private Boolean select5;

    @Column(nullable = true)
    private Boolean select6;

    @Column(nullable = true)
    private Boolean select7;

    @Column(nullable = true)
    private Boolean select8;

    @Column(nullable = true)
    private Boolean select9;

    @Column(nullable = true)
    private Boolean select10;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_question_id")
    private General_question generalQuestion;
}
