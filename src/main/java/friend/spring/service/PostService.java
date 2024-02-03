package friend.spring.service;


import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.web.dto.PostRequestDTO;
import org.springframework.data.domain.Page;

public interface PostService {
    void checkPost(Boolean flag);

    void checkPostWriterUser(Boolean flag);

    Post joinPost(PostRequestDTO.AddPostDTO request, Long userId);

    Boolean checkPoint(PostRequestDTO.AddPostDTO request, User user);

    Page<Post> getMyPostList(Long userId, Integer page);
}

