package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.UserConverter;
import friend.spring.domain.User;
import friend.spring.repository.UserRepository;
import friend.spring.service.EmailService;
import friend.spring.service.UserService;
import friend.spring.service.UserServiceImpl;
import friend.spring.web.dto.UserRequestDTO;
import friend.spring.web.dto.UserResponseDTO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService mailService;

    @GetMapping("/my-page")
    public ApiResponse<UserResponseDTO.MyPageResDTO> myPage(@RequestHeader(name = "id") Long userId) {
        User Page = userService.findMyPage(userId);
        return ApiResponse.onSuccess(UserConverter.toMypageResDTO(Page));
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
    @PostMapping("/join")//회원가입
    public ApiResponse<UserResponseDTO.JoinResultDTO> join(@RequestBody @Valid UserRequestDTO.UserJoinRequest userJoinRequest) {

       User user = userService.joinUser(userJoinRequest);
       return ApiResponse.onSuccess(UserConverter.joinResultDTO(user));

    }
}
