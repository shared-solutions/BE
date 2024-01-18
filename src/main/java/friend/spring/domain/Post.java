package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.domain.mapping.Post_like;
import friend.spring.domain.mapping.Post_scrap;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column
    private PostType postType;

    @Enumerated(EnumType.STRING)
    @Column
    private PostVoteType voteType;

    @Column(nullable = true, length = 1000)
    private String file;

    @Column(nullable = true, length = 100)
    private String tag;

    @Enumerated(EnumType.STRING)
    @Column
    private PostState state;

    @Column(nullable = false)
    private Integer view;

    @Column(nullable = true)
    private Timestamp deadline;

    @Column(nullable = true)
    private Integer point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 부모 글 정의
    // 고민후기 원글 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Post parentPost;

    // 자식 글 정의
    @OneToMany(mappedBy = "parentPost")
    private List<Post> reviewPostList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Alarm> alarmList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Post_like> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Post_scrap> postScrapList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment_choice> commentChoiceList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<General_question> generalQuestionList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Gauge_question> gaugeQuestionList = new ArrayList<>();
}
