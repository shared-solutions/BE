package friend.spring.service;

import friend.spring.domain.Alarm;
import org.springframework.data.domain.Page;

public interface AlarmService {

    void checkAlarm(boolean flag);

    Page<Alarm> getAlarmList(Long userId, Integer page);
}
