package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.UserConverter;
import friend.spring.domain.Comment;
import friend.spring.domain.Level;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.repository.UserRepository;
import friend.spring.service.CommentService;
import friend.spring.service.EmailService;
import friend.spring.service.PostService;
import friend.spring.service.UserService;
import friend.spring.service.UserServiceImpl;
import friend.spring.web.dto.AlarmResponseDTO;
import friend.spring.web.dto.UserRequestDTO;
import friend.spring.web.dto.UserResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService mailService;
    private final PostService postService;
    private final CommentService commentService;

    //마이 페이지 조회
    @GetMapping("/my-page")
    public ApiResponse<UserResponseDTO.MyPageResDTO> getMyPage(
            @RequestHeader(name = "id") Long userId) {
        User myPage = userService.findMyPage(userId);
        return ApiResponse.onSuccess(UserConverter.toMypageResDTO(myPage));
    }

    @PostMapping ("/mailSend")//이메일 인증 코드 전송
    public ApiResponse<UserResponseDTO.EmailSendRes> mailSend(@RequestBody @Valid UserRequestDTO.EmailSendReq emailDto){
        System.out.println("이메일 인증 요청이 들어옴");
        System.out.println("이메일 인증 이메일 :"+emailDto.getEmail());

        String code = mailService.joinEmail(emailDto.getEmail());
        return ApiResponse.onSuccess(UserConverter.toEmailSendRes(code));
    }
    @PostMapping("/mailauthCheck")//이메일 코드 확인
    public ApiResponse<Void> mailauthCheck(@RequestBody @Valid UserRequestDTO.EmailSendCheckReq emailSendCheckReq){

    mailService.CheckAuthNum(emailSendCheckReq.getEmail(), emailSendCheckReq.getAuthNum());
    return ApiResponse.onSuccess(null);
    }

    //나의 Q&A 질문 조회
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

    //나의 Q&A 답변 조회
    @GetMapping("/my-page/profile/answer")
    public ApiResponse<UserResponseDTO.AnswerResDTO> getAnswer(
            @RequestHeader(name = "id") Long userId,
            @RequestParam(name = "page") Integer page){
        User myPage = userService.findMyPage(userId);
        Level nxtLevel = userService.nextLevel(userId);
        Page<Comment> myCommentList = commentService.getMyCommentList(userId, page);
        return ApiResponse.onSuccess(UserConverter.toAnswerResDTO(myPage, nxtLevel, myCommentList));
    }
    @PostMapping("/join")//회원가입
    public ApiResponse<UserResponseDTO.JoinResultDTO> join(@RequestBody @Valid UserRequestDTO.UserJoinRequest userJoinRequest) {

       User user = userService.joinUser(userJoinRequest);
       return ApiResponse.onSuccess(UserConverter.joinResultDTO(user));

    }
    @PostMapping ("/passwordMailSend")//비밀번호 재설정 인증 코드 전송
    @Operation(summary = "비밀번호 재설정 인증 코드 전송 API",description = "비밀번호 재설정 인증 코드 전송하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({ })
    public ApiResponse<UserResponseDTO.EmailSendRes> mailSend(@RequestBody @Valid UserRequestDTO.PasswordEmailSendReq emailDto){
        System.out.println("비밀번호 재설정 인증 요청이 들어옴");
        System.out.println("비밀번호 재설정 인증 이메일 :"+emailDto.getEmail());

        String code = mailService.joinEmail(emailDto.getEmail());
        return ApiResponse.onSuccess(UserConverter.toEmailSendRes(code));
    }
    @PostMapping("/passwordMailauthCheck")//이메일 코드 확인
    @Operation(summary = "비밀번호 재설정 코드 확인 API",description = "비밀번호 재설정 코드 확인하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4005",description = "UNAUTHORIZED, 인증 코드가 일치하지 않습니다."),
    })
    @Parameters({ })
    public ApiResponse<Void> mailauthCheck(@RequestBody @Valid UserRequestDTO.PasswordEmailSendCheckReq emailSendCheckReq){
        mailService.CheckAuthNum(emailSendCheckReq.getEmail(), emailSendCheckReq.getAuthNum());
        return ApiResponse.onSuccess(null);
    }

}

