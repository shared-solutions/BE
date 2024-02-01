package friend.spring.service;

import friend.spring.domain.Comment;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    void checkComment(Boolean flag);
    void checkCommentLike(Boolean flag);
    void checkCommentChoice(Boolean flag);
    void checkSelectCommentAnotherUser(Boolean flag);

    void checkCommentWriterUser(Boolean flag);
    public Comment createComment(Long postId, CommentRequestDTO.commentCreateReq request, Long userId);

    Comment_like likeComment(Long postId, Long commentId, Long userId);

    Page<CommentResponseDTO.commentGetRes> getComments(Long postId, Integer page, Integer size);

    void dislikeComment(Long postId, Long commentId, Long userId);

    Comment_choice selectComment(Long postId, Long commentId, Long userId);

    void editComment(Long postId, Long commentId, CommentRequestDTO.commentEditReq request, Long userId);

    Page<Comment> getMyCommentList(Long userId, Integer page);

}
