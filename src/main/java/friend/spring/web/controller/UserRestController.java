package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.UserConverter;
import friend.spring.domain.Level;
import friend.spring.domain.User;
import friend.spring.service.UserService;
import friend.spring.web.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;

    @GetMapping("/my-page")
    public ApiResponse<UserResponseDTO.MyPageResDTO> myPage(@RequestHeader(name = "id") Long userId) {
        User Page = userService.findMyPage(userId);
        return ApiResponse.onSuccess(UserConverter.toMypageResDTO(Page));
    }
}
