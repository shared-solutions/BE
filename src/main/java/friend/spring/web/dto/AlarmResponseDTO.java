package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class AlarmResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmListResDTO {
        List<AlarmResDTO> alarmList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmResDTO {
        Long alarmId;
        String userNickname;
        String userPhoto;
        String alarmType;
        String alarmContent;
        String commentContent;
        Long postId;
        Long commentId;
        Boolean read;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmLeftResDTO {
        Boolean isAlarmLeft;
    }
}
