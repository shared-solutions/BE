package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.CommentConverter;
import friend.spring.converter.MyPageConverter;
import friend.spring.domain.*;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.service.*;
import friend.spring.web.dto.CommentResponseDTO;
import friend.spring.web.dto.MyPageRequestDTO;
import friend.spring.web.dto.MyPageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/my-page")
public class MyPageRestController {
    private final UserService userService;
    private final PostService postService;
    private final MyPageService myPageService;

    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;

    //저장한 게시물(내 카테고리) 조회
    @GetMapping("/post")
    @Operation(summary = "사용자 글 카테고리 조회 API",description = "사용자의 저장 글 카테고리 목록을 집합으로 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "회원정보가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4004",description = "카테고리를 찾을 수 없습니다.")

    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<MyPageResponseDTO.SavedCategoryResDTO> getCategorySet(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request){
        Long userId = jwtTokenService.JwtToId(request);
        List<Category> categoryList = myPageService.getCategoryList(userId);
        return ApiResponse.onSuccess(MyPageConverter.toSavedCategoryResDTO(categoryList));
    }

    //저장한 게시물(모든게시물)
    @GetMapping("/post/all")
    @Operation(summary = "저장한 게시물(모든게시물) 조회 API",description = "사용자의 전체 저장 글을 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "회원정보가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4001",description = "글을 찾을 수 없습니다.."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4005",description = "저장한 글이 없습니다.")

    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "sort", description = "query string(RequestParam) - 조회순(0)인지 최신순(1)인지 가리키는 sort 변수")

    })
    public ApiResponse<MyPageResponseDTO.SavedAllPostResDTO> getAllSavedPosts(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "sort", defaultValue = "0") Integer sort,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request){
        Long userId = jwtTokenService.JwtToId(request);
        Page<Post> allPostList = myPageService.getAllPostList(userId, page, sort);
        return ApiResponse.onSuccess(MyPageConverter.toSavedAllPostResDTO(allPostList));
    }

    // 회원정보 사용자 프로필 사진 수정
    @PatchMapping(value = "/profile/modify/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원정보 사용자 프로필 사진 수정 API", description = "사용자 프로필 사진을 수정하는 API입니다. ex) /user/my-page/profile/modify/image")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<Void> editUserImage(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        myPageService.editUserImage(file, request);
        return ApiResponse.onSuccess(null);
    }

    // 회원정보 수정 페이지
    @GetMapping(value = "/profile/modify")
    @Operation(summary = "회원정보 수정 페이지 API", description = "회원정보 수정 페이지를 조회하는 API입니다. ex) /user/my-page/profile/modify")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 요청에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
    })
    public ApiResponse<MyPageResponseDTO.MyProfileResDTO> getEditUserPage(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        Long userId = jwtTokenService.JwtToId(request);
        User editUserPage = myPageService.getEditUserPage(userId);
        return ApiResponse.onSuccess(MyPageConverter.toMyProfileResDTO(editUserPage));
    }

    @PatchMapping(value = "/profile/modify/name")
    @Operation(summary = "회원정보 이름 수정 API", description = "회원정보 이름을 수정하는 API입니다. ex) /user/my-page/profile/modify/name")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 요청에 성공했습니다. "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4005",description = "UNAUTHORIZED, 인증 코드가 일치하지 않습니다."),
    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<MyPageResponseDTO.ProfileEditNameRes> editUserName(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request,
            @RequestBody @Valid MyPageRequestDTO.ProfileEditNameReq profileEditNameReq){
        Long userId = jwtTokenService.JwtToId(request);
        emailService.CheckAuthNum(profileEditNameReq.getEmail(), profileEditNameReq.getCertification());
        User editUserName = myPageService.editUserName(userId, profileEditNameReq);
        return ApiResponse.onSuccess(MyPageConverter.toProfileEditNameResDTO(editUserName));
    }

    @PatchMapping(value = "/profile/modify/email")
    @Operation(summary = "회원정보 이메일 수정 API", description = "회원정보 이메일을 수정하는 API입니다. ex) /user/my-page/profile/modify/email")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 요청에 성공했습니다. "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4005",description = "UNAUTHORIZED, 인증 코드가 일치하지 않습니다."),
    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<MyPageResponseDTO.ProfileEditEmailRes> editUserEmail(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request,
            @RequestBody @Valid MyPageRequestDTO.ProfileEditEmailReq profileEditEmailReq){
        Long userId = jwtTokenService.JwtToId(request);
        emailService.CheckAuthNum(profileEditEmailReq.getCurEmail(), profileEditEmailReq.getCertification());
        User editUserEmail = myPageService.editUserEmail(userId, profileEditEmailReq);
        return ApiResponse.onSuccess(MyPageConverter.toProfileEditEmailResDTO(editUserEmail));
    }

    @PatchMapping(value = "/profile/modify/phone")
    @Operation(summary = "회원정보 번호 수정 API", description = "회원정보 번호를 수정하는 API입니다. ex) /user/my-page/profile/modify/phone")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 요청에 성공했습니다. "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4005",description = "UNAUTHORIZED, 인증 코드가 일치하지 않습니다."),
    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<MyPageResponseDTO.ProfileEditPhoneRes> editUserPhone(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request,
            @RequestBody @Valid MyPageRequestDTO.ProfileEditPhoneReq profileEditPhoneReq){
        Long userId = jwtTokenService.JwtToId(request);
        emailService.CheckAuthNum(profileEditPhoneReq.getEmail(), profileEditPhoneReq.getCertification());
        User editUserPhone = myPageService.editUserPhone(userId, profileEditPhoneReq);
        return ApiResponse.onSuccess(MyPageConverter.toProfileEditPhoneResDTO(editUserPhone));
    }

    @PatchMapping(value = "/profile/modify/password")
    @Operation(summary = "회원정보 비밀번호 수정 API", description = "회원정보 비밀번호를 수정하는 API입니다. ex) /user/my-page/profile/modify/password")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 요청에 성공했습니다. "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4008",description = "NOT_FOUND, 비밀번호가 틀렸습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4009",description = "NOT_FOUND, 확인 비밀번호가 일치하지 않습니다."),
    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<MyPageResponseDTO.ProfileEditPasswordRes> editUserPassword(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request,
            @RequestBody @Valid MyPageRequestDTO.ProfileEditPasswordReq profileEditPasswordReq){
        Long userId = jwtTokenService.JwtToId(request);
        User editUserPassword = myPageService.editUserPassword(userId, profileEditPasswordReq);
        return ApiResponse.onSuccess(MyPageConverter.toProfileEditPasswordResDTO(editUserPassword));
    }

    @PatchMapping(value = "/profile/modify/security")
    @Operation(summary = "회원정보 보안 메일 수정 API", description = "회원정보 보안 메일을 수정하는 API입니다. ex) /user/my-page/profile/modify/security")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 요청에 성공했습니다. "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "이미 존재하는 메일 주소입니다.")
    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<MyPageResponseDTO.ProfileEditEmailRes> editUserSecurity(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request,
            @RequestBody @Valid MyPageRequestDTO.ProfileEditSecurityReq profileEditSecurityReq){
        Long userId = jwtTokenService.JwtToId(request);
        emailService.CheckAuthNum(profileEditSecurityReq.getCurEmail(), profileEditSecurityReq.getCertification());
        emailService.CheckAuthNum(profileEditSecurityReq.getChangeEmail(), profileEditSecurityReq.getNxtCertification());
        User editUserSecurity = myPageService.editUserSecurity(userId, profileEditSecurityReq);
        return ApiResponse.onSuccess(MyPageConverter.toProfileEditEmailResDTO(editUserSecurity));
    }

    @PatchMapping(value = "/inquiry")
    @Operation(summary = "설정 문의하기 API", description = "문의를 작성하는 API입니다. ex) /user/my-page/inquiry")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 요청에 성공했습니다. "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4007", description = "최소 5자 이상, 30자 미만 입력해 주세요."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4008", description = "최소 5자 이상, 1000자 미만 입력해 주세요.")
    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<MyPageResponseDTO.MyInquiryRes> editUserSecurity(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request,
            @RequestBody @Valid MyPageRequestDTO.MyInquiryReq myInquiryReq){
        Long userId = jwtTokenService.JwtToId(request);
        Inquiry inquiry = myPageService.createInquiry(userId, myInquiryReq);
        return ApiResponse.onSuccess(MyPageConverter.toMyInquiryRes(inquiry));
    }

    @GetMapping(value = "/post/{category-id}")
    @Operation(summary = "사용자 글 카테고리 상세보기 조회 API", description = "카테고리별로 상세화면을 조회하는 API입니다. ex) /user/my-page/post/{category-id}")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 요청에 성공했습니다. "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001",description = "NOT_FOUND, 사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST4004",description = "NOT_FOUND, 글에 대한 스크랩 데이터를 찾을 수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken"),
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
            @Parameter(name = "category-id", description = "path variable - 카테고리 아이디"),
    })
    public ApiResponse<MyPageResponseDTO.SavedPostCategoryDetailListRes> GetUserCategoryDetail(
            @RequestHeader(name = "atk") String atk,
            @PathVariable("category-id") Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            HttpServletRequest request){
        Long userId = jwtTokenService.JwtToId(request);
        Page<Post> categoryDetailList = myPageService.getCategoryDetailList(userId, categoryId, page);
        Category category = myPageService.getCategory(categoryId);
        return ApiResponse.onSuccess(MyPageConverter.toSavedPostCategoryDetailListRes(categoryDetailList, category));
    }

    @GetMapping(value = "/setting/notice")
    @Operation(summary = "공지사항 리스트 조회 API", description = "전체 공지사항의 리스트를 조회하는 API입니다. ex) /user/my-page/setting/notice")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 요청에 성공했습니다. "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "NOTICE4001",description = "NOT_FOUND, 공지사항이 없습니다."),
    })
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)"),
    })
    public ApiResponse<MyPageResponseDTO.NoticeListRes> getNoticeList(
            @RequestParam(name = "page", defaultValue = "0") Integer page){
        Page<Notice> noticeList = myPageService.getNoticeList(Long.parseLong("30"), page);
        return ApiResponse.onSuccess(MyPageConverter.toNoticeListRes(noticeList));
    }

    @GetMapping(value = "/setting/notice/{notice-id}")
    @Operation(summary = "공지사항 상세 조회 API", description = "전체 공지사항 상세내용을 조회하는 API입니다. ex) /user/my-page/setting/notice/{notice-id}")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 요청에 성공했습니다. "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "NOTICE4001",description = "NOT_FOUND, 공지사항이 없습니다."),
    })
    @Parameters({
    })
    public ApiResponse<MyPageResponseDTO.NoticeDetailRes> getNoticeDetail(
            @PathVariable("notice-id") Long noticeId
            ){
        Notice noticeDetail = myPageService.getNoticeDetail(noticeId);
        return ApiResponse.onSuccess(MyPageConverter.toNoticeDetailRes(noticeDetail));
    }
}
