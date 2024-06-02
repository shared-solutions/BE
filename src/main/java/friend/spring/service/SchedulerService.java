package friend.spring.service;

import friend.spring.apiPayload.GeneralException;
import friend.spring.apiPayload.code.status.ErrorStatus;
import friend.spring.converter.AlarmConverter;
import friend.spring.converter.SseConverter;
import friend.spring.domain.*;
import friend.spring.domain.enums.AlarmType;
import friend.spring.repository.AlarmRepository;
import friend.spring.repository.PostRepository;
import friend.spring.web.dto.SseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final PostRepository postRepository;
    private final EmailService emailService;
    private final SseService notificationService;
    private final AlarmRepository alarmRepository;

    // 투표 마감
    // (1시간마다 검사해서 투표 마감 날짜가 지나면 닫히도록 할 예정)
    @Transactional
    @Async
    @Scheduled(cron = "0 0/1 * * * *", zone = "Asia/Seoul") // 매 분마다 실행 -> 매 시간마다 실행으로 변경 예정
    public void deleteVote() {
        List<Post> postList = postRepository.findAll();
        if (postList.isEmpty()) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }
        LocalDateTime now = LocalDateTime.now();
        for (Post post : postList) {
            Gauge_poll gaugePoll = post.getGaugePoll();
            Card_poll cardPoll = post.getCardPoll();
            General_poll generalPoll = post.getGeneralPoll();
            if (gaugePoll != null && gaugePoll.getDeadline().isBefore(now) && gaugePoll.getVoteOnGoing()) {
                gaugePoll.setVoteOnGoing(false);
                getData(post);
            }
            if (cardPoll != null && cardPoll.getDeadline().isBefore(now) && cardPoll.getVoteOnGoing()) {
                cardPoll.setVoteOnGoing(false);
                getData(post);
            }
            if (generalPoll != null && generalPoll.getDeadline().isBefore(now) && generalPoll.getVoteOnGoing()) {
                generalPoll.setVoteOnGoing(false);
                getData(post);
            }
        }

    }

    private void getData(Post post) {
        SseResponseDTO.VoteFinishResDTO voteFinishResDTO = SseConverter.toVoteFinishResDTO(post, AlarmType.VOTE_FINISH);
        Alarm newAlarm = AlarmConverter.toAlarm(voteFinishResDTO.getAlarmContent(), AlarmType.VOTE_FINISH, post, post.getUser(), null);
        emailService.voteFinishEmail(post);
        alarmRepository.saveAndFlush(newAlarm);
        notificationService.customNotify(post.getUser().getId(), voteFinishResDTO, post.getTitle(), AlarmType.VOTE_FINISH.toString());
    }


}
