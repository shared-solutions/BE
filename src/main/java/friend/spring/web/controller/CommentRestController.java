package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.CommentConverter;
import friend.spring.domain.Comment;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.service.CommentService;
import friend.spring.web.dto.CommentRequestDTO;
import friend.spring.web.dto.CommentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentRestController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{post-id}/comment")
    @Operation(summary = "댓글 작성 API", description = "댓글 작성하는 API입니다. ex) /posts/1/comment")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4001",description = "NOT_FOUND, (부모 루트) 댓글을 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<CommentResponseDTO.commentCreateRes> createComment(
            @PathVariable("post-id") Long postId,
            @RequestBody CommentRequestDTO.commentCreateReq requestBody,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        Comment comment = commentService.createComment(postId, requestBody, request);
        return ApiResponse.onSuccess(CommentConverter.toCommentCreateRes(comment));
    }

    // 댓글 추천(좋아요)
    @PostMapping("/{post-id}/comment/{comment-id}/like")
    @Operation(summary = "댓글 추천(좋아요) 생성 API", description = "댓글 추천(좋아요) 생성하는 API입니다. ex) /posts/1/comment/1/like")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4001",description = "NOT_FOUND, 댓글을 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "comment-id", description = "path variable - 댓글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<CommentResponseDTO.commentLikeRes> likeComment(
            @PathVariable("post-id") Long postId,
            @PathVariable("comment-id") Long commentId,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        Comment_like comment_like = commentService.likeComment(postId, commentId, request);
        return ApiResponse.onSuccess(CommentConverter.toCommentLikeRes(comment_like));
    }

    // 댓글 추천(좋아요) 해제
    @PostMapping("/{post-id}/comment/{comment-id}/like/del")
    @Operation(summary = "댓글 추천(좋아요) 해제 API", description = "댓글 추천(좋아요) 해제하는 API입니다. ex) /posts/1/comment/1/like/del")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4001",description = "NOT_FOUND, 댓글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4002",description = "NOT_FOUND, 댓글에 대한 좋아요 데이터를 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "comment-id", description = "path variable - 댓글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<Void> dislikeComment(
            @PathVariable("post-id") Long postId,
            @PathVariable("comment-id") Long commentId,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        commentService.dislikeComment(postId, commentId, request);
        return ApiResponse.onSuccess(null);
    }

    // 댓글 조회
    @GetMapping("/{post-id}/comments")
    @Operation(summary = "댓글 조회 API", description = "댓글을 조회하는 API입니다. ex) /posts/111/comments?page=0&size=10")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 루트댓글 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)")
    })
    public ApiResponse<Page<CommentResponseDTO.commentGetRes>> getComments(
            @PathVariable("post-id") Long postId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size

    ) {
        return ApiResponse.onSuccess(commentService.getComments(postId, page, size));
    }

    // 댓글 채택
    @PostMapping("/{post-id}/comment/{comment-id}/choice")
    @Operation(summary = "댓글 채택 API", description = "댓글 채택 데이터를 생성하는 API입니다. ex) /posts/1/comment/1/choice")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4002",description = "BAD_REQUEST, 올바른 사용자(글 작성자)가 아닙니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4001",description = "NOT_FOUND, 댓글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4003",description = "BAD_REQUEST, 댓글 채택은 1개 댓글에 대해서만 가능합니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4004",description = "BAD_REQUEST, 자기 자신은 채택할 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "comment-id", description = "path variable - 댓글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<CommentResponseDTO.commentSelectRes> selectComment(
            @PathVariable("post-id") Long postId,
            @PathVariable("comment-id") Long commentId,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        Comment_choice comment_choice = commentService.selectComment(postId, commentId, request);
        return ApiResponse.onSuccess(CommentConverter.toCommentSelectRes(comment_choice));
    }

    // 댓글 수정
    @PatchMapping("/{post-id}/comment/{comment-id}/edit")
    @Operation(summary = "댓글 수정 API", description = "댓글 수정하는 API입니다. ex) /posts/1/comment/1/edit")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4001",description = "NOT_FOUND, 댓글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4005",description = "올바른 사용자(댓글 작성자)가 아닙니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "comment-id", description = "path variable - 댓글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<Void> editComment(
            @PathVariable("post-id") Long postId,
            @PathVariable("comment-id") Long commentId,
            @RequestBody CommentRequestDTO.commentEditReq requestBody,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        commentService.editComment(postId, commentId, requestBody, request);
        return ApiResponse.onSuccess(null);
    }

    // 댓글 삭제
    @PatchMapping("/{post-id}/comment/{comment-id}/like/del")
    @Operation(summary = "댓글 삭제 API", description = "댓글 삭제하는 API입니다. ex) /posts/1/comment/1/del")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4001",description = "NOT_FOUND, 댓글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4005",description = "올바른 사용자(댓글 작성자)가 아닙니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "comment-id", description = "path variable - 댓글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<Void> deleteComment(
            @PathVariable("post-id") Long postId,
            @PathVariable("comment-id") Long commentId,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        commentService.deleteComment(postId, commentId, request);
        return ApiResponse.onSuccess(null);
    }
}
