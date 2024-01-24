package friend.spring.converter;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.enums.PostCategory;
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
                postType=PostType.NOT_VOTE;
                break;
            case 2:
                postType=PostType.VOTE;
                break;
            case 3:
                postType=PostType.REVIEW;
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
            }
        }

        switch (request.getCategory()){
            case 1:
                category=PostCategory.SPORTS;
                break;
            case 2:
                category=PostCategory.ANIMALS;
                break;
            case 3:
                category=PostCategory.FASHION;
                break;
        }


        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .postType(postType)
                .category(category)
                .voteType(postVoteType)
                .file(request.getFile())
                .tag(request.getTag())
                .deadline(request.getDeadline())
                .point(request.getPoint())
                .view(0)
                .scrap(0)
                .build();
    }
}


