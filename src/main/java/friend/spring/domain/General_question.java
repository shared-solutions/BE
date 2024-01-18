package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class General_question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String candidate1;

    @Column(nullable = true)
    private String candidate2;

    @Column(nullable = true)
    private String candidate3;

    @Column(nullable = true)
    private String candidate4;

    @Column(nullable = true)
    private String candidate5;

    @Column(nullable = true)
    private String candidate6;

    @Column(nullable = true)
    private String candidate7;

    @Column(nullable = true)
    private String candidate8;

    @Column(nullable = true)
    private String candidate9;

    @Column(nullable = true)
    private String candidate10;

    @Column(nullable = true)
    private String c1_img;

    @Column(nullable = true)
    private String c2_img;

    @Column(nullable = true)
    private String c3_img;

    @Column(nullable = true)
    private String c4_img;

    @Column(nullable = true)
    private String c5_img;

    @Column(nullable = true)
    private String c6_img;

    @Column(nullable = true)
    private String c7_img;

    @Column(nullable = true)
    private String c8_img;

    @Column(nullable = true)
    private String c9_img;

    @Column(nullable = true)
    private String c10_img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "generalQuestion")
    private List<General_vote> generalVoteList = new ArrayList<>();
}
