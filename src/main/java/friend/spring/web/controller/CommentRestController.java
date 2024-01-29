package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.CommentConverter;
import friend.spring.domain.Comment;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.service.CommentService;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 댓글 조회
    @GetMapping("/{post-id}/comments")
    @Operation(summary = "댓글 조회 API", description = "댓글을 조회하는 API입니다. ex) /posts/111/comments?page=0&size=10")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디 입니다!"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 입니다! (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 루트댓글 몇 개씩 불러올지 개수를 세는 변수입니다. (1 이상 자연수로 설정)")
    })
    public ApiResponse<Page<CommentResponseDTO.commentGetRes>> getComments(
            @PathVariable("post-id") Long postId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size

    ) {
        return ApiResponse.onSuccess(commentService.getComments(postId, page, size));
    }
}
