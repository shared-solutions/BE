package friend.spring.web.controller;
import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.CandidateConverter;
import friend.spring.converter.PostConverter;
import friend.spring.domain.Candidate;
import friend.spring.domain.Post;
import friend.spring.repository.PostRepository;
import friend.spring.service.JwtTokenService;
import friend.spring.service.PostQueryService;
import friend.spring.service.PostService;
import friend.spring.web.dto.*;
import friend.spring.domain.mapping.Post_like;
import friend.spring.domain.mapping.Post_scrap;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/posts")
public class PostRestController {
    private final PostService postService;
    private final PostQueryService postQueryService;
    private final PostRepository postRepository;
    private final JwtTokenService jwtTokenService;
    @PostMapping(value = "/")
    @Operation(summary = "글 작성 API", description = "글을 추가 합니다.")
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
            @Parameter(name="title", description="<String> 글 제목"),
            @Parameter(name="content", description="<String> 글 내용"),
            @Parameter(name="category", description="카테고리. 한글 입력(ex 교육)" ),
            @Parameter(name="postType", description="<Integer> 글 종류<br>1 : VOTE <br>2 : REVIEW"),
            @Parameter(name="postVoteType", description="<Integer> 투표 종류<br>1 : GENERAL <br>2 : GAUGE <br>3 : CARD<br>해당 사항 없을시 null"),
            @Parameter(name="pollTitle", description="<String> 투표 제목"),
            @Parameter(name="multipleChoice", description="<Boolean> 복수 선택 여부"),
            @Parameter(name="parent_id", description="<Long> 원글(후기글 경우) id<br>해당 사항 없을시 null"),
            @Parameter(name="deadline", description="<LocaldateTime> 투표 마감 시간<br>해당 사항 없을시 null(기본값 1시간 이후)"),
            @Parameter(name="point", description="<Integer> 포인트<br>해당 사항 없을시 null")

    })
    public ApiResponse<PostResponseDTO.AddPostResultDTO> join(@RequestPart(value = "request") @Valid PostRequestDTO.AddPostDTO request,
//                                                              @RequestPart(value = "file", required = false) List<MultipartFile> file,
                                                              @RequestHeader("atk") String atk,
                                                              HttpServletRequest request2){
        Post post= postService.joinPost(request,request2);
        return ApiResponse.onSuccess(PostConverter.toAddPostResultDTO(post));
    }

    @PostMapping(value = "/{post-id}")
    @Operation(summary = "후보 생성 API", description = "후보를 생성합니다.(글 작성 API 호출 후 postId 응답 받으시면, 후보 개수 만큼 바로 호출해주세요!)")
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
            @Parameter(name="pollOption", description="<Class> 투표 후보{optionString : string, optionImg : MultipartFile}<br>해당 사항 없을시 null"),
    })
    public ApiResponse<CandidateResponseDTO.AddCandidateResultDTO> createCandidate(
            @PathVariable(name="post-id") Long postId,
            @RequestBody CandidateRequestDTO.AddCandidateRequestDTO request,
            @RequestHeader("atk") String atk,
            HttpServletRequest request2) throws IOException {
        Candidate candidate = postService.createCandidate(postId, request, request2);
        return ApiResponse.onSuccess(CandidateConverter.toAddCandidateResultDTO(candidate));
    }


    @GetMapping("/poll-postList")
    @Operation(summary = "후기글 작성시 내투표 보기 API", description = "후기글 작성시 내투표 보기합니다.")
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 입니다! (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 몇 개씩 불러올지 개수를 세는 변수입니다. (1 이상 자연수로 설정)"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<ParentPostDTO.ParentPostGetListDTO> getParentPosts(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                          @RequestParam(name = "size",defaultValue = "15") Integer size,
                                                                          @RequestHeader("atk") String atk,
                                                                          HttpServletRequest request2){
        Page<Post> postPage=postQueryService.getParentPostList(page,size,request2);
        Long userId=jwtTokenService.JwtToId(request2);
        return ApiResponse.onSuccess(PostConverter.parentPostGetListDTO(postPage,userId));

    }
    @GetMapping("/{post-id}")
    @Operation(summary = "글 상세 보기 API", description = "글 상세 보기합니다<br>response로 나오는 isLike, isComment는 각각 조회하는 사용자의 좋아요 클릭 여부, " +
            "댓글 작성 여부입니다..")
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<PostResponseDTO.PostDetailResponse> getPostDetail(@PathVariable(name="post-id")Long PostId,
                                                                         @RequestHeader("atk") String atk,
                                                                         HttpServletRequest request2){
        Long userId=jwtTokenService.JwtToId(request2);
        Post post =postQueryService.getPostDetail(PostId);
        Post parentPost=postQueryService.ParentPost(PostId);;
        Boolean engage=postQueryService.checkEngage(PostId,userId);
        return ApiResponse.onSuccess(PostConverter.postDetailResponse(post,engage,userId,parentPost));

    }
    @GetMapping("/poll-post/{category}")
    @Operation(summary = "고민글 전체 보기 API", description = "고민글 전체 보기합니다")
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 입니다! (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 몇 개씩 불러올지 개수를 세는 변수입니다. (1 이상 자연수로 설정)"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
            @Parameter(name = "category", description = "path variable - category(한글). 모두 보기는 '모두'라고 입력 하시면 됩니다.")
    })
    public ApiResponse<PostResponseDTO.PollPostGetListDTO> getPostAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                         @RequestParam(name = "size",defaultValue = "15") Integer size,
                                                                         @PathVariable(name = "category") String category,
                                                                         @RequestHeader("atk") String atk,
                                                                         HttpServletRequest request2){
        Long userId=jwtTokenService.JwtToId(request2);
        Page<Post> postPage=postQueryService.getPostList(page,size,category);
        return ApiResponse.onSuccess(PostConverter.pollPostGetListDTO(postPage,userId));

    }

    @GetMapping("/review-post")
    @Operation(summary = "후기글 전체 보기 API", description = "후기글 전체 보기합니다")
    @Parameters({
            @Parameter(name = "arrange", description = "query string(RequestParam) - 정렬 기준 변수입니다. (0: 조회순,1: 최신순) 디폴트값 0"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 입니다! (0부터 시작) 디폴트값 0"),
            @Parameter(name = "size", description = "query string(RequestParam) - 몇 개씩 불러올지 개수를 세는 변수입니다. (1 이상 자연수로 설정) 디폴트값 15"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<PostResponseDTO.ReviewPostGetListDTO> getReviewAll(@RequestParam(name="arrange", defaultValue = "0") Integer arrange,
                                                                             @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                             @RequestParam(name = "size",defaultValue = "15") Integer size,
                                                                             @RequestHeader("atk") String atk,
                                                                             HttpServletRequest request2){
        Long userId=jwtTokenService.JwtToId(request2);
        Page<Post> postPage=postQueryService.getReviewList(page,size,arrange);
        return ApiResponse.onSuccess(PostConverter.reviewPostGetListDTO(postPage,userId));
    }

    @PatchMapping("/{post-id}/edit")
    @Operation(summary = "글 수정 API", description = "댓글 수정하는 API입니다. ex) /posts/1/comment/1/edit")
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<Void> editPost(@PathVariable("post-id") Long postId,
                                      @RequestBody @Valid PostRequestDTO.PostEditReq request,
                                      @RequestHeader("atk") String atk,
                                      HttpServletRequest request2) {
        Long userId=jwtTokenService.JwtToId(request2);
        postService.editPost(postId, request, userId);
        return ApiResponse.onSuccess(null);
    }

    @PatchMapping("/{post-id}/del")
    @Operation(summary = "글 삭제 API", description = "글 삭제하는 API입니다. ex) /posts/1/del")
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<Void> deleteComment(
            @PathVariable("post-id") Long postId,
            @RequestHeader("atk") String atk,
            HttpServletRequest request2
    ) {
        Long userId=jwtTokenService.JwtToId(request2);
        postService.deletePost(postId, userId);
        return ApiResponse.onSuccess(null);
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
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<PostResponseDTO.PostLikeRes> likePost(
            @PathVariable("post-id") Long postId,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        Post_like post_like = postService.likePost(postId, request);
        return ApiResponse.onSuccess(PostConverter.toPostLikeRes(post_like));
    }

    // 글 추천(좋아요) 해제
    @DeleteMapping("/{post-id}/like/del")
    @Operation(summary = "글 추천(좋아요) 해제 API", description = "글 추천(좋아요) 해제하는 API입니다. ex) /posts/1/like/del")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4003",description = "글에 대한 좋아요 데이터를 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<Void> dislikePost(
            @PathVariable("post-id") Long postId,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        postService.dislikePost(postId, request);
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
            @Parameter(name = "size", description = "query string(RequestParam) - 한 번에 글 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<PostResponseDTO.PollPostGetListDTO> getBestPosts(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size,
            @RequestHeader("atk") String atk,
            HttpServletRequest request
    ) {
        return ApiResponse.onSuccess(postService.getBestPosts(page, size, request));
    }

    // 홈 - 답변을 기다리는 고민들
    @GetMapping("/recent")
    @Operation(summary = "홈 - 답변을 기다리는 고민들 API", description = "홈 - 답변을 기다리는 고민들 조회 API입니다. ex) /posts/recent")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
    })
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "size", description = "query string(RequestParam) - 한 번에 글 몇 개씩 불러올지 개수를 세는 변수 (1 이상 자연수로 설정)"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<PostResponseDTO.PollPostGetListDTO> getRecentPosts(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size,
            @RequestHeader("atk") String atk,
            HttpServletRequest request
    ) {
        return ApiResponse.onSuccess(postService.getRecentPosts(page, size, request));
    }

    // 글 스크랩 추가
    @PostMapping("/{post-id}/scrap")
    @Operation(summary = "글 스크랩 추가 API", description = "글 스크랩 추가하는 API입니다. ex) /posts/1/scrap")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<PostResponseDTO.ScrapCreateRes> createScrapPost(
            @PathVariable("post-id") Long postId,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        Post_scrap post_scrap = postService.createScrapPost(postId, request);
        return ApiResponse.onSuccess(PostConverter.toScrapCreateRes(post_scrap));
    }

    // 글 스크랩 해제
    @DeleteMapping("/{post-id}/scrap/del")
    @Operation(summary = "글 스크랩 해제 API", description = "글 스크랩 해제하는 API입니다. ex) /posts/1/scrap/del")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4004",description = "글에 대한 스크랩 데이터를 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "post-id", description = "path variable - 글 아이디"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<Void> deleteScrapPost(
            @PathVariable("post-id") Long postId,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        postService.deleteScrapPost(postId, request);
        return ApiResponse.onSuccess(null);
    }
}
