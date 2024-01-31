package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.UserConverter;
import friend.spring.domain.Comment;
import friend.spring.domain.Level;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.repository.PostRepository;
import friend.spring.service.CommentService;
import friend.spring.service.PostService;
import friend.spring.service.UserService;
import friend.spring.web.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    @GetMapping("/my-page")
    public ApiResponse<UserResponseDTO.MyPageResDTO> getMyPage(
            @RequestHeader(name = "id") Long userId) {
        User myPage = userService.findMyPage(userId);
        return ApiResponse.onSuccess(UserConverter.toMypageResDTO(myPage));
    }

    @GetMapping("/my-page/profile/question")
    public ApiResponse<UserResponseDTO.QuestionResDTO> getQuestion(
            @RequestHeader(name = "id") Long userId,
            @RequestParam(name = "page") Integer page){
        User myPage = userService.findMyPage(userId);
        Level nxtLevel = userService.nextLevel(userId);
        Page<Post> myPostList = postService.getMyPostList(userId, page);
        Page<Comment> myCommentList = commentService.getMyCommentList(userId, page);
        return ApiResponse.onSuccess(UserConverter.toQuestionResDTO(myPage, nxtLevel, myPostList, myCommentList));
    }

    @GetMapping("/my-page/profile/answer")
    public ApiResponse<UserResponseDTO.AnswerResDTO> getAnswer(
            @RequestHeader(name = "id") Long userId,
            @RequestParam(name = "page") Integer page){
        User myPage = userService.findMyPage(userId);
        Level nxtLevel = userService.nextLevel(userId);
        Page<Comment> myCommentList = commentService.getMyCommentList(userId, page);
        return ApiResponse.onSuccess(UserConverter.toAnswerResDTO(myPage, nxtLevel, myCommentList));
    }
}
