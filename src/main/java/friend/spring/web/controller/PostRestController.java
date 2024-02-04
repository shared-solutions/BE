package friend.spring.web.controller;
import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.CommentConverter;
import friend.spring.converter.PostConverter;
import friend.spring.domain.Post;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.domain.mapping.Post_like;
import friend.spring.service.PostService;
import friend.spring.web.dto.CommentResponseDTO;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/posts")
public class PostRestController {
    private final PostService postService;
    @PostMapping("/{user-id}")
    @Operation(summary = "글 작성 API", description = "글을 추가 합니다.")
    @Parameters({
            @Parameter(name="title", description="<String> 글 제목"),
            @Parameter(name="content", description="<String> 글 내용"),
            @Parameter(name="category", description="<Integer> 카테고리<br>1 : EDUCATION <br>2 : ENTERTAINMENT <br>3 : LIFESTYLE <br>4 : ECONOMY <br>5 : SHOPPING" +
                    "<br>6 : OTHERS" ),
            @Parameter(name="postType", description="<Integer> 글 종류<br>1 : NOT_VOTE <br>2 : VOTE <br>3 : REVIEW"),
            @Parameter(name="postVoteType", description="<Integer> 투표 종류<br>1 : GENERAL <br>2 : GAUGE <br>3 : CARD<br>해당 사항 없을시 null"),
            @Parameter(name="pollTitle", description="<String> 투표 제목"),
            @Parameter(name="multipleChoice", description="<Boolean> 복수 선택 여부"),
            @Parameter(name="pollOption", description="<Class> 투표 후보{optionString : string, optionImg : string}<br>해당 사항 없을시 null"),
            @Parameter(name="parent_id", description="<Long> 원글(후기글 경우) id<br>해당 사항 없을시 null"),
            @Parameter(name="deadline", description="<Timestamp> 투표 마감 시간<br>해당 사항 없을시 null"),
            @Parameter(name="point", description="<Integer> 포인트<br>해당 사항 없을시 null")

    })
    public ApiResponse<PostResponseDTO.AddPostResultDTO> join(@RequestBody @Valid PostRequestDTO.AddPostDTO request,
                                                              @PathVariable(name="user-id")Long UserId){
        Post post= postService.joinPost(request,UserId);
        return ApiResponse.onSuccess(PostConverter.toAddPostResultDTO(post));
    }

    // 글 추천(좋아요) 생성
    @PostMapping("/{post-id}/like")
    @Operation(summary = "글 추천(좋아요) 생성 API", description = "글 추천(좋아요) 생성하는 API입니다. ex) /posts/1/like")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "userId", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
    })
    public ApiResponse<PostResponseDTO.PostLikeRes> likePost(
            @PathVariable("post-id") Long postId,
            @RequestHeader("userId") Long userId
    ) {
        Post_like post_like = postService.likePost(postId, userId);
        return ApiResponse.onSuccess(PostConverter.toPostLikeRes(post_like));
    }

    // 글 추천(좋아요) 해제
    @PostMapping("/{post-id}/like/del")
    @Operation(summary = "글 추천(좋아요) 해제 API", description = "글 추천(좋아요) 해제하는 API입니다. ex) /posts/1/like/del")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4003",description = "글에 대한 좋아요 데이터를 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "userId", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
    })
    public ApiResponse<Void> dislikePost(
            @PathVariable("post-id") Long postId,
            @RequestHeader("userId") Long userId
    ) {
        postService.dislikePost(postId, userId);
        return ApiResponse.onSuccess(null);
    }

    // 홈 - 지금 가장 핫한 고민투표
    @GetMapping("/best")
    @Operation(summary = "홈 - 지금 가장 핫한 고민투표 API", description = "홈 - 지금 가장 핫한 고민투표 조회 API입니다. ex) /posts/best")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
    })
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 한 번에 글 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)")
    })
    public ApiResponse<Page<PostResponseDTO.PostSummaryListRes>> getBestPosts(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size
    ) {
        return ApiResponse.onSuccess(postService.getBestPosts(page, size));
    }

    // 홈 - 답변을 기다리는 고민들
    @GetMapping("/recent")
    @Operation(summary = "홈 - 답변을 기다리는 고민들 API", description = "홈 - 답변을 기다리는 고민들 조회 API입니다. ex) /posts/recent")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
    })
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 한 번에 글 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)")
    })
    public ApiResponse<Page<PostResponseDTO.PostSummaryListRes>> getRecentPosts(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size
    ) {
        return ApiResponse.onSuccess(postService.getRecentPosts(page, size));
    }

//    // 스크랩 추가
//    @PostMapping("/{post-id}/scrap")
//    @Operation(summary = "스크랩 추가 API", description = "스크랩 추가하는 API입니다. ex) /posts/1/like")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
//    })
//    @Parameters({
//            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
//            @Parameter(name = "userId", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
//    })
//    public ApiResponse<PostResponseDTO.PostLikeRes> createScrapPost(
//            @PathVariable("post-id") Long postId,
//            @RequestHeader("userId") Long userId
//    ) {
//        Post_like post_like = postService.likePost(postId, userId);
//        return ApiResponse.onSuccess(PostConverter.toPostLikeRes(post_like));
//    }
//
//    // 스크랩 해제
//    @PostMapping("/{post-id}/like/del")
//    @Operation(summary = "글 추천(좋아요) 해제 API", description = "글 추천(좋아요) 해제하는 API입니다. ex) /posts/1/like/del")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4003",description = "글에 대한 좋아요 데이터를 찾을 수 없습니다."),
//    })
//    @Parameters({
//            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
//            @Parameter(name = "userId", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)"),
//    })
//    public ApiResponse<Void> deleteScrapPost(
//            @PathVariable("post-id") Long postId,
//            @RequestHeader("userId") Long userId
//    ) {
//        postService.dislikePost(postId, userId);
//        return ApiResponse.onSuccess(null);
//    }
}
