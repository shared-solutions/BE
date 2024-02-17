package friend.spring.converter;

import friend.spring.domain.Comment;
import friend.spring.domain.Post;
import friend.spring.domain.enums.AlarmType;
import friend.spring.web.dto.SseResponseDTO;

public class SseConverter {
    public static SseResponseDTO.CommentCreateResDTO toCommentCreateResDTO (Comment comment, AlarmType alarmType) {
        String userPhoto = null;
        if (comment.getUser().getFile() != null) {
            userPhoto = comment.getUser().getFile().getUrl();
        }

        String alarmContent = comment.getUser().getNickname();
        if (alarmType.equals(AlarmType.COMMENT)) {
            alarmContent += "님이 회원님의 게시물에 댓글을 남겼습니다.";
        } else {
            alarmContent += "님이 회원님의 댓글에 답글을 남겼습니다.";
        }

        return SseResponseDTO.CommentCreateResDTO.builder()
                .userNickname(comment.getUser().getNickname())
                .userPhoto(userPhoto)
                .alarmContent(alarmContent)
                .commentContent(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .alarmType(alarmType.toString())
                .postId(comment.getPost().getId())
                .commentId(comment.getId())
                .build();
    }

    public static SseResponseDTO.VoteFinishResDTO toVoteFinishResDTO(Post post, AlarmType alarmType){
        return SseResponseDTO.VoteFinishResDTO.builder()
                .postId(post.getId())
                .alarmContent(post.getContent())
                .alarmType(alarmType.toString())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
