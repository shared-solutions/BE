package friend.spring.converter;

import friend.spring.domain.Comment;
import friend.spring.domain.Level;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.web.dto.CommentResponseDTO;
import friend.spring.web.dto.PostResponseDTO;
import friend.spring.web.dto.UserResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {
    public static UserResponseDTO.MyPageResDTO toMypageResDTO(User user){
        return UserResponseDTO.MyPageResDTO.builder()
                .userPhoto(user.getImage())
                .userName(user.getNickname())
                .userPoint(user.getPoint())
                .userLevelInt(user.getLevel().getLike())
                .userLevelName(user.getLevel().getName())
                .userRecommend(user.getLike())
                .build();
    }


    public static UserResponseDTO.EmailSendRes toEmailSendRes(String code) {
        return UserResponseDTO.EmailSendRes.builder()
                .code(code)
                .build();
    }
    //나의 프로필(Q&A질문)
    public static UserResponseDTO.QuestionResDTO toQuestionResDTO(User user, Level nxtLevel, Page<Post> postList, Page<Comment> commentList){
        //질문 목록
        List<PostResponseDTO.MyPostDTO> myPostDTOList = postList.stream()
                .map(PostConverter::toMyPostResDTO).collect(Collectors.toList());

        //총 답변수
        int all = commentList.getSize();
        //채택 답변수
        List<Comment> myCommentList = commentList.stream()
                .filter(comment -> comment.getCommentChoiceList().isEmpty()).collect(Collectors.toList());
        //답변 채택률
        int aChoice = all - myCommentList.size();
        double aChoicePercent = ((double)aChoice / (double)all) * 100;

        //질문 채택률
        int Question = myPostDTOList.size();
        List<Post> myPostList = postList.stream()
                .filter(post -> post.getCommentChoiceList().isEmpty()).collect(Collectors.toList());
        double pChoicePercent = ((double) (Question - myPostList.size()) / (double) Question)*100;

        //남은 다음 등급
        double nxtGrade = ((double)user.getLike()/(double)(nxtLevel.getLike() - user.getLevel().getLike())) * 100.0;
        return UserResponseDTO.QuestionResDTO.builder()
                .userPhoto(user.getImage())
                .nickName(user.getNickname())
                .recommend(user.getLike())
                .grade(user.getLevel().getName())
                .nextGrade(nxtGrade)
                .nextGradeName(nxtLevel.getName())
                .adoptComments(aChoice)
                .adoptCommentPercent(aChoicePercent)
                .postNum(myPostDTOList.size())
                .adoptPostPercent(pChoicePercent)
                .postList(myPostDTOList)
                .build();
    }

    //나의 프로필(Q&A답변)
    public static UserResponseDTO.AnswerResDTO toAnswerResDTO(User user, Level nxtLevel, Page<Comment> commentList){
        //답변 목록
        List<CommentResponseDTO.myCommentRes> myCommentDTOList = commentList.stream()
                .map(CommentConverter::toMyCommentResDTO).collect(Collectors.toList());
        //총 답변수
        int all = myCommentDTOList.size();

        //채택 답변수
        List<Comment> myCommentList = commentList.stream()
                .filter(comment -> comment.getCommentChoiceList().isEmpty()).collect(Collectors.toList());
        int choice = all - myCommentList.size();
        //채택 답변률
        double percent = ((double)choice / (double)all) * 100.0;

        //남은 다음 등급
        double nxtGrade = ((double)user.getLike()/(double)(nxtLevel.getLike() - user.getLevel().getLike())) * 100.0;
        return UserResponseDTO.AnswerResDTO.builder()
                .userPhoto(user.getName())
                .nickName(user.getNickname())
                .recommend(user.getLike())
                .grade(user.getLevel().getName())
                .nextGrade(nxtGrade)
                .nextGradeName(nxtLevel.getName())
                .adoptComments(choice)
                .adoptCommentPercent(percent)
                .commentList(myCommentDTOList)
                .build();
    }


}
