package friend.spring.converter;

import friend.spring.domain.Comment;
import friend.spring.domain.Level;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.mapping.Comment_choice;
import friend.spring.web.dto.PostResponseDTO;
import friend.spring.web.dto.UserResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {
    public static UserResponseDTO.MyPageResDTO toMypageResDTO(User user){
        return UserResponseDTO.MyPageResDTO.builder()
                .userPhoto(user.getImage())
                .userName(user.getName())
                .userPoint(user.getPoint())
                .userLevelInt(user.getLevel().getLike())
                .userLevelName(user.getLevel().getName())
                .userRecommend(user.getLike())
                .build();
    }

    //나의 프로필(Q&A질문)
    public static UserResponseDTO.QuestionResDTO toQuestionResDTO(User user, Level nxtLevel, Comment comment, Page<Post> postList){
        List<PostResponseDTO.MyPostDTO> myPostDTOList = postList.stream()
                .map(PostConverter::toMyPostDTO).collect(Collectors.toList());
        double nxtGrade = ((double)user.getLike()/(double)(nxtLevel.getLike() - user.getLevel().getLike())) * 100.0;
        myPostDTOList.stream().
        return UserResponseDTO.QuestionResDTO.builder()
                .userPhoto(user.getImage())
                .nickName(user.getNickname())
                .recommend(user.getLike())
                .grade(user.getLevel().getName())
                .nextGrade(nxtGrade)
                .nextGradeName(nxtLevel.getName())
                .adoptComments(comment.getCommentChoiceList().size())
                .adoptCommentPercent()
                .postNum(myPostDTOList.size())
                .adoptPostPercent()
                .postList(myPostDTOList)
                .build();
    }

}
