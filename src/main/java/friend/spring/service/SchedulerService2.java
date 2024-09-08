package friend.spring.service;

import friend.spring.domain.Point;
import friend.spring.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService2 {
    private final PointRepository pointRepository;

//    // Point 내역 합계 값과 User 엔티티의 point 값이 일치하는지 주기적으로 확인
//    @Transactional
//    @Async
//    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 매일 오전 0시에 실행
//    public void checkPoint() {
//        List<Point> pointList = pointRepository.findAll();
//        for (Point point : pointList) {
//
//        }
//
//
//        chatRoomRepository.disabledSafeUpdates(); // safe update 임시 해제 (여러 열 수정 가능하도록)
//        chatRoomRepository.deleteChatRoom();
//    }
}
