package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class MyPageResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResDTO{
        String category;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SavedCategoryResDTO{
        List<CategoryResDTO> postCategoryList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SavedPostResDTO{
        Long ago;
        String title;
        String content;
        Integer postLike;
        Integer comment;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SavedAllPostResDTO{
        List<SavedPostResDTO> postList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyProfileResDTO{
        String userImage;
        String nickName;
        String email;
        String phone;
    }
}
