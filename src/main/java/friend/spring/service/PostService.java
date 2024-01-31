package friend.spring.service;

import friend.spring.domain.Post;
import org.springframework.data.domain.Page;

public interface PostService {
    void checkPost(Boolean flag);

    Page<Post> getMyPostList(Long userId, Integer page);

}
