package friend.spring.kakao.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.apiPayload.GeneralException;
import friend.spring.kakao.service.AuthService;
import friend.spring.service.JwtTokenService;
import friend.spring.service.UserService;
import friend.spring.web.dto.TokenDTO;
import friend.spring.web.dto.UserResponseDTO;
//import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 및 회원 가입을 진행")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @GetMapping("/login/kakao")
    public ApiResponse<List<TokenDTO>> kakaoLogin(@RequestParam("code") String code) throws GeneralException {

        return ApiResponse.onSuccess(authService.kakaoLogin(code));
    }
}
//    @GetMapping("/login/kakao")
//    public BaseResponse<OAuthResponse> kakaoLogin(@RequestParam("code") String code) {
//        return BaseResponse.onSuccess(authService.kakaoLogin(code));
//    }