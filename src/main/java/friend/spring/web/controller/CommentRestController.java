package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.CommentConverter;
import friend.spring.domain.Comment;
import friend.spring.service.CommentService;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return ApiResponse.onSuccess(CommentConverter.toCreateCommentRes(comment));
    }
}
