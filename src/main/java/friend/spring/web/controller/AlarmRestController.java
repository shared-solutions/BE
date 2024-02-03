package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.AlarmConverter;
import friend.spring.domain.Alarm;
import friend.spring.service.AlarmService;
import friend.spring.web.dto.AlarmResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AlarmRestController {

    private final AlarmService alarmService;
    //알림 조회
    @GetMapping("/alarm")
    @Operation(summary = "사용자 알림 조회 API",description = "사용자의 알림 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ALARM4001",description = "NOT_FOUND, 알림을 찾을 수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)")
    })
    private ApiResponse<AlarmResponseDTO.AlarmListResDTO> getAlarm(
            @RequestHeader(name = "id") Long userId,
            @RequestParam(name = "page") Integer page){
        Page<Alarm> alarmList = alarmService.getAlarmList(userId, page);
        return ApiResponse.onSuccess(AlarmConverter.toAlarmListResDTO(alarmList));
    }
}
