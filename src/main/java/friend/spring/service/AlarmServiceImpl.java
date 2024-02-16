package friend.spring.service;

import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.apiPayload.handler.AlarmHandler;
import friend.spring.domain.Alarm;
import friend.spring.domain.User;
import friend.spring.repository.AlarmRepository;
import friend.spring.repository.UserRepository;
import friend.spring.security.JwtTokenProvider;
import friend.spring.web.dto.AlarmResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmServiceImpl implements AlarmService{

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
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

    @Override
    public AlarmResponseDTO.AlarmLeftResDTO getRemainingAlarm(HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Boolean isAlarmLeft = alarmRepository.existsByUserIdAndReadIsFalse(userId);
        return AlarmResponseDTO.AlarmLeftResDTO.builder()
                .isAlarmLeft(isAlarmLeft).build();
    }

    @Override
    @Transactional
    public void editAlarmRead(Long alarmId, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getCurrentUser(request);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            userService.checkUser(false);
        }

        Optional<Alarm> optionalAlarm = alarmRepository.findById(alarmId);
        if (optionalAlarm.isEmpty()) {
            checkAlarm(false);
        }

        Alarm alarm = optionalAlarm.get();
        alarm.setRead(true);
    }
}
