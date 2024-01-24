package friend.spring.service;


import friend.spring.domain.Post;
import friend.spring.web.dto.PostRequestDTO;

public interface PostService {
    void checkPost(Boolean flag);
    Post joinPost(PostRequestDTO.AddPostDTO request, Long userId);
}

