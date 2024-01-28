package friend.spring.converter;

import friend.spring.domain.Comment;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;

public class CommentConverter {
    public static Comment toComment(CommentRequestDTO.commentCreateReq request, Post post, User user, Comment parentComment) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .parentComment(parentComment)
                .build();
    }

    public static CommentResponseDTO.commentCreateRes toCommentCreateRes(Comment comment) {
        return CommentResponseDTO.commentCreateRes.builder()
                .commentId(comment.getId())
                .build();
    }

    public static Comment_like toCommentLike(Post post, Comment comment, User user) {
        return Comment_like.builder()
                .user(user)
                .comment(comment)
                .build();
    }

    public static CommentResponseDTO.commentLikeRes toCommentLikeRes(Comment_like comment_like) {
        return CommentResponseDTO.commentLikeRes.builder()
                .commentLikeId(comment_like.getId())
                .build();
    }
}
