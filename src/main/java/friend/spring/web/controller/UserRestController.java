package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.converter.UserConverter;
import friend.spring.domain.Comment;
import friend.spring.domain.Level;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.repository.UserRepository;
import friend.spring.service.*;
import friend.spring.web.dto.AlarmResponseDTO;
import friend.spring.web.dto.TokenDTO;
import friend.spring.web.dto.UserRequestDTO;
import friend.spring.web.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService mailService;
    private final PostService postService;
    private final CommentService commentService;
    private final JwtTokenService jwtTokenService;
    //마이 페이지 조회
    @GetMapping("/my-page")
    public ApiResponse<UserResponseDTO.MyPageResDTO> getMyPage(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request) {
        Long userId=jwtTokenService.JwtToId(request);
        User myPage = userService.findMyPage(userId);
        return ApiResponse.onSuccess(UserConverter.toMypageResDTO(myPage));
    }

    @PostMapping ("/mailSend")//이메일 인증 코드 전송
    @Operation(summary = "이메일 인증 코드 전송 API",description = "이메일 인증 코드 전송하는 API입니다.")
    public ApiResponse<UserResponseDTO.EmailSendRes> mailSend(@RequestBody @Valid UserRequestDTO.EmailSendReq emailDto){
        System.out.println("이메일 인증 요청이 들어옴");
        System.out.println("이메일 인증 이메일 :"+emailDto.getEmail());

        String code = mailService.joinEmail(emailDto.getEmail());
        return ApiResponse.onSuccess(UserConverter.toEmailSendRes(code));
    }
    @PostMapping("/mailauthCheck")//이메일 코드 확인
    @Operation(summary = "이메일 코드 확인 API",description = "이메일 코드 확인하는 API입니다.")
    public ApiResponse<Void> mailauthCheck(@RequestBody @Valid UserRequestDTO.EmailSendCheckReq emailSendCheckReq){

    mailService.CheckAuthNum(emailSendCheckReq.getEmail(), emailSendCheckReq.getAuthNum());
    return ApiResponse.onSuccess(null);
    }

    //나의 Q&A 질문 조회
    @GetMapping("/my-page/profile/question")
    @Operation(summary = "나의 Q&A 질문 조회 API",description = "사용자의 나의 Q&A 질문 조회 API 이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "NOT_FOUND, 글을 찾을 수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)")
    })
    public ApiResponse<UserResponseDTO.QuestionResDTO> getQuestion(
            HttpServletRequest request,
            @RequestParam(name = "page") Integer page){
        Long userId=jwtTokenService.JwtToId(request);
        User myPage = userService.findMyPage(userId);
        Level nxtLevel = userService.nextLevel(userId);
        Page<Post> myPostList = postService.getMyPostList(userId, page);
        Page<Comment> myCommentList = commentService.getMyCommentList(userId, page);
        return ApiResponse.onSuccess(UserConverter.toQuestionResDTO(myPage, nxtLevel, myPostList, myCommentList));
    }

    //나의 Q&A 답변 조회
    @GetMapping("/my-page/profile/answer")
    @Operation(summary = "나의 Q&A 답변 조회 API",description = "사용자의 Q&A 답변 조회 API 이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT4001",description = "NOT_FOUND, 댓글을 찾을 수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)")
    })
    public ApiResponse<UserResponseDTO.AnswerResDTO> getAnswer(
           HttpServletRequest request,
            @RequestParam(name = "page") Integer page){
        Long userId=jwtTokenService.JwtToId(request);
        User myPage = userService.findMyPage(userId);
        Level nxtLevel = userService.nextLevel(userId);
        Page<Comment> myCommentList = commentService.getMyCommentList(userId, page);
        return ApiResponse.onSuccess(UserConverter.toAnswerResDTO(myPage, nxtLevel, myCommentList));
    }
    @PostMapping("/join")//회원가입
    @Operation(summary = "회원가입 API",description = "회원가입하는 API입니다.")
    public ApiResponse<UserResponseDTO.JoinResultDTO> join(@RequestBody @Valid UserRequestDTO.UserJoinRequest userJoinRequest) {

       User user = userService.joinUser(userJoinRequest);
       return ApiResponse.onSuccess(UserConverter.joinResultDTO(user));

    }

    //로그인
    @PostMapping("/login")
    @Operation(summary = "로그인 API",description = "로그인하는 API입니다.")
    public ApiResponse<List<TokenDTO>> login(@RequestBody UserRequestDTO.UserLoginRequest userLoginRequest)throws GeneralException{
        List<TokenDTO> tokenDTOList = userService.login(userLoginRequest);
        return ApiResponse.onSuccess(tokenDTOList);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급 API",description = "토큰 재발급하는 API입니다.")
    public ApiResponse<List<TokenDTO>> reissue(@RequestHeader(name = "rtk") String rtk, HttpServletRequest request) {
        System.out.println("controller: reissue 함수 실행");
        return ApiResponse.onSuccess(userService.reissue(request));
    }

    // 로그아웃
    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API",description = "로그아웃하는 API입니다.")
    public ApiResponse<String> logout(@RequestHeader(name = "atk") String atk, HttpServletRequest request)  {
        return ApiResponse.onSuccess(userService.logout(request));
    }

    @GetMapping("/point")
    @Operation(summary = "포인트 조회 API", description = "임시로 user-id 입력")
    public ApiResponse<UserResponseDTO.PointViewDTO> myPoint(@RequestHeader(name = "id") Long userId) {
        Integer point = userService.pointCheck(userId);
        return ApiResponse.onSuccess(UserConverter.toPointViewResDTO(point));
    }
}

