package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import friend.spring.domain.enums.Gender;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.domain.mapping.Post_like;
import friend.spring.domain.mapping.Post_scrap;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String pw;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false, length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Column(nullable = false, length = 30)
    private String nickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private Boolean event;

    @Column(nullable = true)
    private String image;

    @Column(nullable = false)
    private Boolean is_deleted;

    @Column(nullable = true)
    private Integer point;

    @Column(nullable = true)
    private String kakao;

    @Column(nullable = false)
    private Integer like;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private Level level;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Point> pointList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alarm> alarmList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Report> reportList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post_like> postLikeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post_scrap> postScrapList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Comment_like> commentLikeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<General_vote> generalVoteList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Gauge_vote> gaugeVoteList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Card_vote> cardVoteList = new ArrayList<>();

    public void setPoint(Integer point) {
        this.point = point;
    }
}
