package friend.spring.converter;

import friend.spring.domain.Alarm;
import friend.spring.web.dto.AlarmResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmConverter {

    //알림
    public static AlarmResponseDTO.AlarmResDTO toAlarmResDTO(Alarm alarm){
        return AlarmResponseDTO.AlarmResDTO.builder()
                .nickName(alarm.getUser().getNickname())
                .type(alarm.getType())
                .content(alarm.getContent())
                .createdAt(alarm.getCreatedAt())
                .build();
    }

    public static AlarmResponseDTO.AlarmListResDTO toAlarmListResDTO(Page<Alarm> alarmList){
        List<AlarmResponseDTO.AlarmResDTO> alarmResDTOList = alarmList.stream()
                .map(AlarmConverter::toAlarmResDTO).collect(Collectors.toList());
        return AlarmResponseDTO.AlarmListResDTO.builder()
                .alarmList(alarmResDTOList)
                .build();
    }
}
