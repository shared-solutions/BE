package friend.spring.service;

import friend.spring.domain.Comment;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    void checkComment(Boolean flag);
    public Comment createComment(Long postId, CommentRequestDTO.commentCreateReq request, Long userId);

    Comment_like likeComment(Long postId, Long commentId, Long userId);

    List<CommentResponseDTO.commentGetRes> getComments(Long postId, Integer page);
}
