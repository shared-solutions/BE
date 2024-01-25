package friend.spring.service;

import friend.spring.domain.Comment;
import friend.spring.web.dto.CommentRequestDTO;

public interface CommentService {
    void checkComment(Boolean flag);
    public Comment createComment(Long postId, CommentRequestDTO.commentCreateReq request, Long userId);
}
