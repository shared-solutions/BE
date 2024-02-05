package friend.spring.service;

import friend.spring.domain.Category;
import friend.spring.domain.Post;
import friend.spring.domain.enums.PostCategory;
import friend.spring.domain.mapping.Post_scrap;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface MyPageService {
    void checkPost(Boolean flag);
    void checkScrapPost(Boolean flag);
    List<Category> getScrapList(Long userId);
}
