package friend.spring.converter;

import friend.spring.domain.Post;
import friend.spring.web.dto.PostResponseDTO;

public class PostConverter {
    public static PostResponseDTO.MyPostDTO toMyPostResDTO(Post post){
        return PostResponseDTO.MyPostDTO.builder()
                .nickName(post.getUser().getNickname())
                .createdAt(post.getCreatedAt())
                .title(post.getTitle())
                .postLike(post.getPostLikeList().size())
                .comment(post.getCommentList().size())
                .build();
    }
}
