package friend.spring.converter;

import friend.spring.domain.Comment;
import friend.spring.domain.Post;
import friend.spring.domain.User;
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

    public static CommentResponseDTO.commentCreateRes toCreateCommentRes(Comment comment) {
        return CommentResponseDTO.commentCreateRes.builder()
                .commentId(comment.getId())
                .build();
    }

    public static CommentResponseDTO.myCommentRes toMyCommentRes()
}
