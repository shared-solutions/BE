package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.AlarmConverter;
import friend.spring.domain.Alarm;
import friend.spring.service.AlarmService;
import friend.spring.service.JwtTokenService;
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

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AlarmRestController {

    private final AlarmService alarmService;
    private final JwtTokenService jwtTokenService;

    //알림 조회
    @GetMapping("/alarm")
    @Operation(summary = "사용자 알림 조회 API", description = "사용자의 알림 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ALARM4001", description = "NOT_FOUND, 알림을 찾을 수 없습니다."),

    })
    @Parameters({
            @Parameter(name = "page", description = "query string(RequestParam) - 몇번째 페이지인지 가리키는 page 변수 (0부터 시작)")
    })
    private ApiResponse<AlarmResponseDTO.AlarmListResDTO> getAlarm(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request,
            @RequestParam(name = "page") Integer page) {
        Long userId = jwtTokenService.JwtToId(request);
        Page<Alarm> alarmList = alarmService.getAlarmList(userId, page);
        return ApiResponse.onSuccess(AlarmConverter.toAlarmListResDTO(alarmList));
    }

    // 홈 - 안 읽은 알림 존재 여부 조회
    @GetMapping("/alarm/notReadAlarm")
    @Operation(summary = "홈 - 안 읽은 알림 존재 여부 조회 API", description = "사용자가 안 읽은 알림이 존재하는지 여부를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({})
    private ApiResponse<AlarmResponseDTO.AlarmLeftResDTO> getRemainingAlarm(
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        return ApiResponse.onSuccess(alarmService.getRemainingAlarm(request));
    }

    // 알림 읽음 처리
    @PatchMapping("/alarm/{alarm-id}")
    @Operation(summary = "사용자 알림 읽음 처리 API", description = "사용자의 알림을 읽음 처리하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ALARM4001", description = "NOT_FOUND, 알림을 찾을 수 없습니다."),
    })
    @Parameters({
            @Parameter(name = "alarm-id", description = "path variable - 알람 아이디"),
    })
    private ApiResponse<Void> editAlarmRead(
            @PathVariable("alarm-id") Long alarmId,
            @RequestHeader(name = "atk") String atk,
            HttpServletRequest request
    ) {
        alarmService.editAlarmRead(alarmId, request);
        return ApiResponse.onSuccess(null);
    }
}
