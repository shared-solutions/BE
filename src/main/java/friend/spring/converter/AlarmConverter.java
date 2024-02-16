package friend.spring.converter;

import friend.spring.domain.Alarm;
import friend.spring.domain.Comment;
import friend.spring.domain.Post;
import friend.spring.domain.User;
import friend.spring.domain.enums.AlarmType;
import friend.spring.web.dto.AlarmResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmConverter {

    //알림
    public static AlarmResponseDTO.AlarmResDTO toAlarmResDTO(Alarm alarm){
        Long commentId = null;
        String commentContent = null;
        String userNickname = null;
        String userPhoto = null;
        if (alarm.getComment() != null) {
            commentId = alarm.getComment().getId();
            commentContent = alarm.getComment().getContent();
            userNickname = alarm.getComment().getUser().getNickname();
            if (alarm.getComment().getUser().getFile() != null) {
                userPhoto = alarm.getComment().getUser().getFile().getUrl();
            }
        }

        return AlarmResponseDTO.AlarmResDTO.builder()
                .alarmId(alarm.getId())
                .userNickname(userNickname)
                .userPhoto(userPhoto)
                .alarmType(alarm.getType().toString())
                .alarmContent(alarm.getContent())
                .postId(alarm.getPost().getId())
                .commentId(commentId)
                .commentContent(commentContent)
                .createdAt(alarm.getCreatedAt())
                .read(alarm.getRead())
                .build();
    }

    public static AlarmResponseDTO.AlarmListResDTO toAlarmListResDTO(Page<Alarm> alarmList){
        List<AlarmResponseDTO.AlarmResDTO> alarmResDTOList = alarmList.stream()
                .map(AlarmConverter::toAlarmResDTO).collect(Collectors.toList());
        return AlarmResponseDTO.AlarmListResDTO.builder()
                .alarmList(alarmResDTOList)
                .build();
    }

    public static Alarm toAlarm(String alarmContent, AlarmType alarmType, Post post, User user, Comment comment) {
        return Alarm.builder()
                .content(alarmContent)
                .type(alarmType)
                .post(post)
                .user(user)
                .comment(comment)
                .read(false)
                .build();
    }
}
