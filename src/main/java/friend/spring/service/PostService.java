package friend.spring.service;


import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Post_like;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    void checkPost(Boolean flag);

    void checkPostWriterUser(Boolean flag);

    void checkPostLike(Boolean flag);

    Post joinPost(PostRequestDTO.AddPostDTO request, Long userId);

    Boolean checkPoint(PostRequestDTO.AddPostDTO request, User user);

    Page<Post> getMyPostList(Long userId, Integer page);

    Post_like likePost(Long postId, Long userId);

    void dislikePost(Long postId, Long userId);

    Page<PostResponseDTO.PostSummaryListRes> getBestPosts(Integer page, Integer size);

    Page<PostResponseDTO.PostSummaryListRes> getRecentPosts(Integer page, Integer size);

    List<PostResponseDTO.PostSummaryListRes> getPostRes(Page<Post> postPage);
}

