package friend.spring.web.controller;
import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.PostConverter;
import friend.spring.domain.Post;
import friend.spring.service.PostService;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
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
    public ApiResponse<PostResponseDTO.AddPostResultDTO> join(@RequestBody @Valid PostRequestDTO.AddPostDTO request,
                                                              @PathVariable(name="user-id")Long UserId){
        Post post= postService.joinPost(request,UserId);
        return ApiResponse.onSuccess(PostConverter.toAddPostResultDTO(post));
    }
}
