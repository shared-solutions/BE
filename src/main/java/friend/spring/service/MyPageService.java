package friend.spring.service;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.User;
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
    List<Category> getCategoryList(Long userId);

    Page<Post> getAllPostList(Long userId,  Integer page, Integer sort);

    void editUserImage(MultipartFile file, HttpServletRequest request);

    User getEditUserPage(Long userId);

    User editUserName(Long userId, MyPageRequestDTO.ProfileEditNameReq profileEditNameReq);

    User editUserEmail(Long userId, MyPageRequestDTO.ProfileEditEmailReq profileEditEmailReq);
    User editUserPhone(Long userId, MyPageRequestDTO.ProfileEditPhoneReq profileEditPhoneReq);

    User editUserPassword(Long userId, MyPageRequestDTO.ProfileEditPasswordReq profileEditPasswordReq);
}
