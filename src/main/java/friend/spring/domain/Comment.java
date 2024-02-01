package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import friend.spring.domain.enums.CommentState;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.domain.mapping.Comment_like;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column
    private CommentState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 부모 댓글 정의
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    // 자식 댓글 정의
    @Builder.Default
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> subCommentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "comment")
    private List<Comment_choice> commentChoiceList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "comment")
    private List<Comment_like> commentLikeList = new ArrayList<>();

    public void update(String content) {
        this.content = content;
    }
}
