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


    @Enumerated(EnumType.STRING)
    @Column
    private PostState state;

    @Builder.Default
    @Column(nullable = false)
    private Integer view=0;


    @Column(nullable = true)
    private Integer point;

//    @Column(nullable = true)
//    private String file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // 부모 글 정의
    // 고민후기 원글 아이디
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Post parentPost;

    @Builder.Default
    // 자식 글 정의
    @OneToMany(mappedBy = "parentPost")
    private List<Post> reviewPostList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Alarm> alarmList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Post_like> postLikeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Post_scrap> postScrapList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment_choice> commentChoiceList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<File> fileList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardPoll_id")
    private Card_poll cardPoll;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="gaugePoll_id")
    private Gauge_poll gaugePoll;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generalPoll_id")
    private General_poll generalPoll;



    public void setUser(User user){
        if(this.user != null)
            user.getPostList().remove(this);
        this.user = user;
        user.getPostList().add(this);
    }

    public void setParentPost(Post parent){
        if(this.parentPost != null)
            parent.getReviewPostList().remove(this);
        this.parentPost=parent;
        parent.getReviewPostList().add(this);
    }

    public void setCategory(Category category){
        if(this.category != null)
            category.getPostList().remove(this);
        this.category =category;
        category.getPostList().add(this);
    }

    public void setView(Integer view) {
        this.view = view;
    }


    public void setGeneralPoll(General_poll generalPoll) {
        this.generalPoll = generalPoll;
        if (generalPoll != null && generalPoll.getPost() != this) {
            generalPoll.setPost(this);
        }
    }

    public void setGaugePoll(Gauge_poll gaugePoll) {
        this.gaugePoll = gaugePoll;
        if (gaugePoll != null && gaugePoll.getPost() != this) {
            gaugePoll.setPost(this);
        }
    }

    public void setCardPoll(Card_poll cardPoll) {
        this.cardPoll = cardPoll;
        if (cardPoll != null && cardPoll.getPost() != this) {
            cardPoll.setPost(this);
        }
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setContent(String content){
        this.content=content;
    }

    public void setStateDel(){
        this.state=PostState.DELETED;
    }
}
