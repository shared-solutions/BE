package friend.spring.converter;

import friend.spring.domain.*;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.web.dto.MyPageRequestDTO;
import friend.spring.web.dto.MyPageResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Calendar.HOUR;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.MINUTE;
public class MyPageConverter {
    public static MyPageResponseDTO.CategoryResDTO toCategoryResDTO(Category category){
        return MyPageResponseDTO.CategoryResDTO.builder()
                .category(category.getName())
                .build();
    }

    public static MyPageResponseDTO.SavedCategoryResDTO toSavedCategoryResDTO(List<Category> categoryList){
        List<MyPageResponseDTO.CategoryResDTO> categoryNameList = categoryList.stream()
                .map(MyPageConverter::toCategoryResDTO).collect(Collectors.toList());
        return MyPageResponseDTO.SavedCategoryResDTO.builder()
                .postCategoryList(categoryNameList).build();
    }

    public static MyPageResponseDTO.SavedPostResDTO toSavedPostResDTO(Post post){
        long diffTime = post.getCreatedAt().until(LocalDateTime.now(), ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -
        diffTime = diffTime / SECOND;
        diffTime = diffTime / MINUTE;
        diffTime = diffTime / HOUR;
        return MyPageResponseDTO.SavedPostResDTO.builder()
                .ago(diffTime)
                .title(post.getTitle())
                .content(post.getContent())
                .postLike(post.getPostLikeList().size())
                .comment(post.getCommentList().size()).build();
    }

    public static MyPageResponseDTO.SavedAllPostResDTO toSavedAllPostResDTO(Page<Post> postList){
        List<MyPageResponseDTO.SavedPostResDTO> savedAllPostList = postList.stream().map(MyPageConverter::toSavedPostResDTO).collect(Collectors.toList());
        return MyPageResponseDTO.SavedAllPostResDTO.builder()
                .postList(savedAllPostList).build();
    }

    public static MyPageResponseDTO.MyProfileResDTO toMyProfileResDTO(User user){
        String userPhoto = null;
        if(user.getFile() != null){
            userPhoto = user.getFile().getUrl();
        }
        return MyPageResponseDTO.MyProfileResDTO.builder()
                .userImage(userPhoto)
                .nickName(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    public static User toUserName(MyPageRequestDTO.ProfileEditNameReq profileEditNameReq){
        return User.builder()
                .nickname(profileEditNameReq.getNickName())
                .build();
    }
    public static MyPageResponseDTO.ProfileEditNameRes toProfileEditNameResDTO(User user){
        return MyPageResponseDTO.ProfileEditNameRes.builder()
                .nickName(user.getNickname())
                .build();
    }

    public static MyPageResponseDTO.ProfileEditEmailRes toProfileEditEmailResDTO(User user){
        return MyPageResponseDTO.ProfileEditEmailRes.builder()
                .changeEmail(user.getEmail())
                .build();
    }

    public static User toUserEmail(MyPageRequestDTO.ProfileEditEmailReq profileEditEmailReq){
        return User.builder()
                .email(profileEditEmailReq.getChangeEmail())
                .build();
    }

    public static MyPageResponseDTO.ProfileEditPhoneRes toProfileEditPhoneResDTO(User user){
        return MyPageResponseDTO.ProfileEditPhoneRes.builder()
                .phone(user.getPhone())
                .build();
    }

    public static MyPageResponseDTO.ProfileEditPasswordRes toProfileEditPasswordResDTO(User user){
        return MyPageResponseDTO.ProfileEditPasswordRes.builder()
                .changePassword(user.getPassword())
                .build();
    }

    public static Inquiry toInquiry(MyPageRequestDTO.MyInquiryReq myInquiryReq, User user){
        return Inquiry.builder()
                .category(myInquiryReq.getInquiryCategory())
                .content(myInquiryReq.getContent())
                .user(user)
                .build();
    }

    public static MyPageResponseDTO.MyInquiryRes toMyInquiryRes(Inquiry inquiry){
        return MyPageResponseDTO.MyInquiryRes.builder()
                .inquiry_id(inquiry.getId())
                .build();
    }

    public static MyPageResponseDTO.SavedPostCategoryDetailRes toSavedPostCategoryDetailRes(Post post){
        long diffTime = post.getCreatedAt().until(LocalDateTime.now(), ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -
        diffTime = diffTime / SECOND;
        diffTime = diffTime / MINUTE;
        diffTime = diffTime / HOUR;
        return MyPageResponseDTO.SavedPostCategoryDetailRes.builder()
                .ago(diffTime)
                .title(post.getTitle())
                .content(post.getContent())
                .postLike(post.getPostLikeList().size())
                .comment(post.getCommentList().size())
                .build();
    }

    public static MyPageResponseDTO.SavedPostCategoryDetailListRes toSavedPostCategoryDetailListRes(Page<Post> postList, Category category){
        List<MyPageResponseDTO.SavedPostCategoryDetailRes> postCategoryDetailResList = postList.stream().map(MyPageConverter::toSavedPostCategoryDetailRes).collect(Collectors.toList());
        return MyPageResponseDTO.SavedPostCategoryDetailListRes.builder()
                .name(category.getName())
                .postList(postCategoryDetailResList)
                .build();
    }

    public static MyPageResponseDTO.NoticeRes toNoticeRes(Notice notice){
        long diffTime = notice.getCreatedAt().until(LocalDateTime.now(), ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -
        diffTime = diffTime / SECOND;
        diffTime = diffTime / MINUTE;
        diffTime = diffTime / HOUR;
        String adminImage = null;
        if (notice.getUser().getFile() != null){
            adminImage = notice.getUser().getFile().getUrl();
        }
        return MyPageResponseDTO.NoticeRes.builder()
                .adminImage(adminImage)
                .ago(diffTime)
                .title(notice.getTitle())
                .content(notice.getContent())
                .build();
    }

    public static MyPageResponseDTO.NoticeListRes toNoticeListRes(Page<Notice> noticeList){
        List<MyPageResponseDTO.NoticeRes> noticeResList = noticeList.stream().map(MyPageConverter::toNoticeRes).collect(Collectors.toList());
        return MyPageResponseDTO.NoticeListRes.builder()
                .noticeList(noticeResList)
                .build();
    }

    public static MyPageResponseDTO.NoticeDetailRes toNoticeDetailRes(Notice notice){
        String adminImage = null;
        if (notice.getUser().getFile() != null){
            adminImage = notice.getUser().getFile().getUrl();
        }
        return MyPageResponseDTO.NoticeDetailRes.builder()
                .adminImage(adminImage)
                .adminName(notice.getUser().getNickname())
                .createdAt(notice.getCreatedAt())
                .content(notice.getContent())
                .view(notice.getView())
                .build();
    }

    public static MyPageResponseDTO.TermRes toTermRes(Term term){
        return MyPageResponseDTO.TermRes.builder()
                .content(term.getTerm())
                .build();
    }

    public static MyPageResponseDTO.PrivacyRes toPrivacyRes(Term term){
        return MyPageResponseDTO.PrivacyRes.builder()
                .content(term.getPrivacy())
                .build();
    }
}
