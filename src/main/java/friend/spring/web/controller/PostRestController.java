package friend.spring.web.controller;
import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.PostConverter;
import friend.spring.domain.Post;
import friend.spring.service.PostService;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
    @Operation(summary = "글 작성 API", description = "PostType = 1 : NOT_VOTE, 2 : VOTE, 3 : REVIEW<br>" +
            "Category = 1 : EDUCATION, 2 : ENTERTAINMENT, 3 : LIFESTYLE, 4 : ECONOMY, 5 : SHOPPING, 6 : OTHERS<br>" +
            "postVoteType = 1 : GENERAL, 2 : GAUGE, 3 : CARD")
    public ApiResponse<PostResponseDTO.AddPostResultDTO> join(@RequestBody @Valid PostRequestDTO.AddPostDTO request,
                                                              @PathVariable(name="user-id")Long UserId){
        Post post= postService.joinPost(request,UserId);
        return ApiResponse.onSuccess(PostConverter.toAddPostResultDTO(post));
    }
}
