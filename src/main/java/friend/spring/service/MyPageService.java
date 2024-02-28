package friend.spring.service;

import friend.spring.domain.*;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.web.dto.MyPageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MyPageService {
    void checkPost(Boolean flag);
    List<Category> getCategoryList(HttpServletRequest request);

    Page<Post> getAllPostList(HttpServletRequest request,  Integer page, Integer sort);

    void editUserImage(MultipartFile file, HttpServletRequest request);

    User getEditUserPage(HttpServletRequest request);

    User editUserName(HttpServletRequest request, MyPageRequestDTO.ProfileEditNameReq profileEditNameReq);

    User editUserEmail(HttpServletRequest request, MyPageRequestDTO.ProfileEditEmailReq profileEditEmailReq);
    User editUserPhone(HttpServletRequest request, MyPageRequestDTO.ProfileEditPhoneReq profileEditPhoneReq);

    User editUserPassword(HttpServletRequest request, MyPageRequestDTO.ProfileEditPasswordReq profileEditPasswordReq);
    User editUserSecurity(HttpServletRequest request, MyPageRequestDTO.ProfileEditSecurityReq profileEditSecurityReq);

    Inquiry createInquiry(HttpServletRequest request, MyPageRequestDTO.MyInquiryReq myInquiryReq);
    Page<Post> getCategoryDetailList(HttpServletRequest request, Long categoryId, Integer page);

    Category getCategory(Long categoryId);

    Page<Notice> getNoticeList(Long userid, Integer page);

    User checkAdmin(Long adminId);

    Notice getNoticeDetail(Long noticeId);

    Term getTerm(Long userId);

    Term getPrivacy(Long userId);
}
