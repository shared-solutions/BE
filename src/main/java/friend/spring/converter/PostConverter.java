package friend.spring.converter;
import friend.spring.domain.Post;
import friend.spring.domain.enums.PostCategory;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;

import java.time.LocalDateTime;

import static friend.spring.domain.enums.PostType.*;

public class PostConverter {

    public static PostResponseDTO.AddPostResultDTO toAddPostResultDTO(Post post) {
        return PostResponseDTO.AddPostResultDTO.builder()
                .postId(post.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Post toPost(PostRequestDTO.AddPostDTO request) {
        PostType postType=null;
        PostVoteType postVoteType=null;
        PostCategory category=null;

        switch (request.getPostType()){
            case 1:
                postType=PostType.VOTE;
                break;
            case 2:
                postType=PostType.REVIEW;
                break;
            default:
                break;
        }

        if(postType== VOTE) {
            switch (request.getPostVoteType()) {
                case 1:
                    postVoteType = PostVoteType.GENERAL;
                    break;
                case 2:
                    postVoteType = PostVoteType.GAUGE;
                    break;
                case 3:
                    postVoteType = PostVoteType.CARD;
                    break;
                default:
                    break;
            }
        }

        switch (request.getCategory()){
            case 1:
                category=PostCategory.EDUCATION;
                break;
            case 2:
                category=PostCategory.ENTERTAINMENT;
                break;
            case 3:
                category=PostCategory.LIFESTYLE;
                break;
            case 4:
                category=PostCategory.ECONOMY;
                break;
            case 5:
                category=PostCategory.SHOPPING;
                break;
            case 6:
                category=PostCategory.OTHERS;
                break;
            default:
                break;
        }


        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .postType(postType)
                .category(category)
                .voteType(postVoteType)
                .point(request.getPoint())
                .view(0)
                .state(PostState.POSTING)
                .build();
    }

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


