package friend.spring.converter;

import friend.spring.domain.Comment;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.enums.CommentState;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentConverter {
    public static Comment toComment(CommentRequestDTO.commentCreateReq request, Post post, User user, Comment parentComment) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .parentComment(parentComment)
                .state(CommentState.POSTING)
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

    public static CommentResponseDTO.commentGetRes toCommentGetRes(Comment comment, Long loginUserId, Boolean isPushedLike, Boolean isOwnerOfPost, Boolean isSelected, List<CommentResponseDTO.commentGetRes> subComments) {
        Long parentCommentId = null;
        if (comment.getParentComment() != null) {
            parentCommentId = comment.getParentComment().getId();
        }

        // 유저 프로필
        String userPhoto = null;
        if (comment.getUser().getFile() != null) {
            userPhoto = comment.getUser().getFile().getUrl();
        }

        // 댓글 삭제 여부
        boolean isDeleted;
        if (comment.getState().equals(CommentState.DELETED)) {
            isDeleted = true;
        } else {
            isDeleted = false;
        }

        // 내가 쓴 댓글인지 여부
        boolean isMyComment;
        if (Objects.equals(comment.getUser().getId(), loginUserId)) {
            isMyComment = true;
        } else {
            isMyComment = false;
        }

        return CommentResponseDTO.commentGetRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickname())
                .userImage(userPhoto)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .parentCommentId(parentCommentId)
                .commentLike(comment.getCommentLikeList().size())
                .childrenComments(subComments)
                .isDeleted(isDeleted)
                .isMyComment(isMyComment)
                .isPushedLike(isPushedLike)
                .isOwnerOfPost(isOwnerOfPost)
                .isSelected(isSelected)
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
