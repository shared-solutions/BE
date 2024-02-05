package friend.spring.service;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.AlarmHandler;
import friend.spring.domain.Alarm;
import friend.spring.domain.User;
import friend.spring.repository.AlarmRepository;
import friend.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmServiceImpl implements AlarmService{

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final UserService userService;
    @Override
    public void checkAlarm(boolean flag) {
        if (!flag){
            throw new AlarmHandler(ErrorStatus.ALARM_NOT_FOUND);
        }
    }

    @Override
    public Page<Alarm> getAlarmList(Long userId, Integer page) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }
        User user = optionalUser.get();

        Page<Alarm> alarmPage = alarmRepository.findAllByUser(user, PageRequest.of(page, 10));
        return alarmPage;

    }
}
