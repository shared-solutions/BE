package friend.spring.web.dto;

import friend.spring.domain.enums.AlarmType;
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
    public static class AlarmListResDTO{
        List<AlarmResDTO> alarmList;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmResDTO{
        String nickName;
        AlarmType type;
        String content;
        LocalDateTime createdAt;
    }
}
