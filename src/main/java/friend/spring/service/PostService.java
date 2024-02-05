package friend.spring.service;


import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Post_like;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {
    void checkPost(Boolean flag);


    void checkPostWriterUser(Boolean flag);

    void checkPostLike(Boolean flag);

    Post joinPost(PostRequestDTO.AddPostDTO request, HttpServletRequest request2);

    Boolean checkPoint(PostRequestDTO.AddPostDTO request, User user);


    void editPost(Long postId,PostRequestDTO.PostEditReq request, Long userId);

    void deletePost(Long postId, Long userId);

    Page<Post> getMyPostList(Long userId, Integer page);

    Post_like likePost(Long postId, Long userId);

    void dislikePost(Long postId, Long userId);

    void checkPostScrap(Boolean flag);

    Page<PostResponseDTO.PostSummaryListRes> getBestPosts(Integer page, Integer size);

    Page<PostResponseDTO.PostSummaryListRes> getRecentPosts(Integer page, Integer size);

    List<PostResponseDTO.PostSummaryListRes> getPostRes(Page<Post> postPage);

    Post_scrap createScrapPost(Long postId, HttpServletRequest request);

    void deleteScrapPost(Long postId, HttpServletRequest request);

}

