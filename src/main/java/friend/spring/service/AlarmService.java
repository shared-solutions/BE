package friend.spring.service;

import friend.spring.domain.Alarm;
import friend.spring.web.dto.AlarmResponseDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;

public interface AlarmService {

    void checkAlarm(boolean flag);

    Page<Alarm> getAlarmList(HttpServletRequest request, Integer page);

    AlarmResponseDTO.AlarmLeftResDTO getRemainingAlarm(HttpServletRequest request);

    void editAlarmRead(Long alarmId, HttpServletRequest request);
}
