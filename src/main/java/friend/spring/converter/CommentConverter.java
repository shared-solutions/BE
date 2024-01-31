package friend.spring.converter;

import friend.spring.domain.Comment;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;

import java.util.List;
import java.util.Optional;

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

    public static CommentResponseDTO.myCommentRes toMyCommentResDTO(Comment comment){
        return CommentResponseDTO.myCommentRes.builder()
                .nickName(comment.getUser().getNickname())
                .createdAt(comment.getCreatedAt())
                .content(comment.getContent())
                .commentLike(comment.getCommentLikeList().size())
                .reComment(comment.getSubCommentList().size())
                .build();
    }
}
