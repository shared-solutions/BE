package friend.spring.service;

import friend.spring.domain.Comment;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {
    void checkComment(Boolean flag);
    void checkCommentLike(Boolean flag);
    void checkCommentChoice(Boolean flag);
    void checkSelectCommentAnotherUser(Boolean flag);

    void checkCommentWriterUser(Boolean flag);
    public Comment createComment(Long postId, CommentRequestDTO.commentCreateReq requestBody, HttpServletRequest request);

    Comment_like likeComment(Long postId, Long commentId, HttpServletRequest request);

    List<CommentResponseDTO.commentGetRes> getComments(Long postId, HttpServletRequest request);

    void dislikeComment(Long postId, Long commentId, HttpServletRequest request);

    Comment_choice selectComment(Long postId, Long commentId, HttpServletRequest request);

    void editComment(Long postId, Long commentId, CommentRequestDTO.commentEditReq requestBody, HttpServletRequest request);

    Page<Comment> getMyCommentList(Long userId, Integer page);

    void deleteComment(Long postId, Long commentId, HttpServletRequest request);
}
