package friend.spring.converter;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.web.dto.MyPageResponseDTO;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyPageConverter {

    public static MyPageResponseDTO.SavedCategoryResDTO toSavedCategoryResDTO(List<Category> categoryList){
        List<String> categoryNameList = new ArrayList<>();
        categoryNameList = categoryList.stream().map(category -> {
            return category.getName();
        }).collect(Collectors.toList());

        return MyPageResponseDTO.SavedCategoryResDTO.builder()
                .postCategoryList(categoryNameList).build();
    }
}
