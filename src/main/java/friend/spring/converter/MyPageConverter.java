package friend.spring.converter;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.User;
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
}
