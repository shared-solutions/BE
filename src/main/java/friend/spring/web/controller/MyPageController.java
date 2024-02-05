package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.MyPageConverter;
import friend.spring.domain.Category;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.service.MyPageService;
import friend.spring.service.PostService;
import friend.spring.service.UserService;
import friend.spring.web.dto.MyPageResponseDTO;
import friend.spring.web.dto.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/my-page")
public class MyPageController {
    private UserService userService;
    private PostService postService;
    private MyPageService myPageService;

    //저장한 게시물(내 카테고리) 조회
    @GetMapping("/post")
    @Operation(summary = "사용자 글 카테고리 조회 API",description = "사용자의 저장 글 카테고리 목록을 집합으로 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "회원정보가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4004",description = "카테고리를 찾을 수 없습니다.")

    })
    @Parameters({
            @Parameter(name = "userId", description = "RequestHeader - 로그인한 사용자 아이디(accessToken으로 변경 예정)")
    })
    public ApiResponse<MyPageResponseDTO.SavedCategoryResDTO> getCategorySet(
            @RequestHeader(name = "userId") Long userId){
        List<Category> scrapList = myPageService.getScrapList(userId);
        return ApiResponse.onSuccess(MyPageConverter.toSavedCategoryResDTO(scrapList));
    }

}
