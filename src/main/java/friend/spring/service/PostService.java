package friend.spring.service;


import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.web.dto.PostRequestDTO;

public interface PostService {
    void checkPost(Boolean flag);

    Post joinPost(PostRequestDTO.AddPostDTO request, Long userId);

    Boolean checkPoint(PostRequestDTO.AddPostDTO request, User user);

    void editPost(Long postId,PostRequestDTO.PostEditReq request, Long userId);

    void deletePost(Long postId, Long userId);
}

