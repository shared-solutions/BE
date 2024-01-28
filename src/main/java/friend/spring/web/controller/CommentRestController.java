package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.CommentConverter;
import friend.spring.domain.Comment;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.service.CommentService;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentRestController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{post-id}/comment")
    public ApiResponse<CommentResponseDTO.commentCreateRes> createComment(
            @PathVariable("post-id") Long postId,
            @RequestBody CommentRequestDTO.commentCreateReq request,
            @RequestHeader("userId") Long userId
    ) {
        Comment comment = commentService.createComment(postId, request, userId);
        return ApiResponse.onSuccess(CommentConverter.toCommentCreateRes(comment));
    }

    // 댓글 추천(좋아요)
    @PostMapping("/{post-id}/comment/{comment-id}/like")
    public ApiResponse<CommentResponseDTO.commentLikeRes> likeComment(
            @PathVariable("post-id") Long postId,
            @PathVariable("comment-id") Long commentId,
            @RequestHeader("userId") Long userId
    ) {
        Comment_like comment_like = commentService.likeComment(postId, commentId, userId);
        return ApiResponse.onSuccess(CommentConverter.toCommentLikeRes(comment_like));
    }
}
