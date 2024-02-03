package friend.spring.converter;

import friend.spring.domain.Comment;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;

import java.util.ArrayList;
import java.util.List;

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

    public static CommentResponseDTO.commentGetRes toCommentGetRes(Comment comment) {
        Long parentCommentId = null;
        if (comment.getParentComment() != null) {
            parentCommentId = comment.getParentComment().getId();
        }

        List<CommentResponseDTO.commentGetRes> subComments = new ArrayList<>();
        if (comment.getSubCommentList() != null) {
            for (Comment c : comment.getSubCommentList()) {
                subComments.add(toCommentGetRes(c));
            }
        }

        return CommentResponseDTO.commentGetRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickname())
                .userImage(comment.getUser().getImage())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .parentCommentId(parentCommentId)
                .commentLike(comment.getCommentLikeList().size())
                .childrenComments(subComments)
                .build();
    }

    public static Comment_choice toCommentChoice(Post post, Comment comment) {
        return Comment_choice.builder()
                .point(post.getPoint())
                .post(post)
                .comment(comment)
                .build();
    }

    public static CommentResponseDTO.commentSelectRes toCommentSelectRes(Comment_choice comment_choice) {
        return CommentResponseDTO.commentSelectRes.builder()
                .commentChoiceId(comment_choice.getId())
                .point(comment_choice.getPoint())
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
