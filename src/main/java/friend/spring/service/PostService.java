package friend.spring.service;


import friend.spring.domain.Candidate;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Post_like;
import friend.spring.domain.mapping.Post_scrap;
import friend.spring.web.dto.CandidateRequestDTO;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface PostService {
    void checkPost(Boolean flag);


    void checkPostWriterUser(Boolean flag);

    void checkPostLike(Boolean flag);

    Post joinPost(PostRequestDTO.AddPostDTO request, HttpServletRequest request2);

    Candidate createCandidate(Long postId, CandidateRequestDTO.AddCandidateRequestDTO request, HttpServletRequest request2) throws IOException;

    Boolean checkPoint(PostRequestDTO.AddPostDTO request, User user);


    void editPost(Long postId, PostRequestDTO.PostEditReq request, Long userId);

    void deletePost(Long postId, Long userId);

    Page<Post> getMyPostList(HttpServletRequest request, Integer page);

    Post_like likePost(Long postId, HttpServletRequest request);

    void dislikePost(Long postId, HttpServletRequest request);

    void checkPostScrap(Boolean flag);

    PostResponseDTO.PollPostGetListDTO getBestPosts(Integer page, Integer size, HttpServletRequest request);

    PostResponseDTO.PollPostGetListDTO getRecentPosts(Integer page, Integer size, HttpServletRequest request);

    Post_scrap createScrapPost(Long postId, HttpServletRequest request);

    void deleteScrapPost(Long postId, HttpServletRequest request);
}

