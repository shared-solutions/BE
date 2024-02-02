package friend.spring.web.controller;
import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.PostConverter;
import friend.spring.domain.Post;
import friend.spring.repository.PostRepository;
import friend.spring.service.PostQueryService;
import friend.spring.service.PostService;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/posts")
public class PostRestController {
    private final PostService postService;
    private final PostQueryService postQueryService;
    private final PostRepository postRepository;
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

    @GetMapping("/{post-id}/{user-id}")
    @Operation(summary = "글 상세 보기 API", description = "글 상세 보기합니다.")
    public ApiResponse<PostResponseDTO.PostDetailResponse> getPostDetail(@PathVariable(name="post-id")Long PostId,
                                                                         @PathVariable(name="user-id")Long userId){
        Optional<Post> postOptional =postQueryService.getPostDetail(PostId);
//        Optional<Post> postOptional =postRepository.findById(PostId);
        Boolean engage=postQueryService.
        Post post = postOptional.get();
        return ApiResponse.onSuccess(PostConverter.postDetailResponse(post,engage));

    }
}
