package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.VoteConverter;
import friend.spring.domain.Card_vote;
import friend.spring.domain.Gauge_vote;
import friend.spring.domain.General_vote;
import friend.spring.security.JwtTokenProvider;
import friend.spring.service.JwtTokenService;
import friend.spring.service.VoteService;
import friend.spring.web.dto.VoteRequestDTO;
import friend.spring.web.dto.VoteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/posts")
public class VoteRestController {
    private final JwtTokenService jwtTokenService;
    private final VoteService voteService;

    @PostMapping("/{post-id}/generalVote")
    @Operation(summary = "일반 투표 API", description = "일반 투표에 참여합니다.")
    @Parameters({
            @Parameter(name = "selectList", description = "<List> 투표 후보 id 리스트"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<VoteResponseDTO.GeneralVoteResponseDTO> join(@RequestBody @Valid VoteRequestDTO.GeneralVoteRequestDTO request,
                                                                    @PathVariable(name = "post-id") Long PostId,
                                                                    @RequestHeader("atk") String atk,
                                                                    HttpServletRequest request2) {
        Long UserId = jwtTokenService.JwtToId(request2);
        General_vote generalVote = voteService.castGeneralVote(request, PostId, UserId);
        return ApiResponse.onSuccess(VoteConverter.toAddGeneralVoteResultDTO(generalVote));
    }

    @PostMapping("/{post-id}/gaugeVote")
    @Operation(summary = "게이지 투표 API", description = "게이지 투표에 참여합니다")
    @Parameters({
            @Parameter(name = "gauge", description = "<Integer> 게이지"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<VoteResponseDTO.GaugeVoteResponseDTO> join(@RequestBody @Valid VoteRequestDTO.GaugeVoteRequestDTO request,
                                                                  @PathVariable(name = "post-id") Long PostId,
                                                                  @RequestHeader("atk") String atk,
                                                                  HttpServletRequest request2
    ) {
        Long UserId = jwtTokenService.JwtToId(request2);
        Gauge_vote gaugeVote = voteService.castGaugeVote(request, PostId, UserId);
        return ApiResponse.onSuccess(VoteConverter.toAddGaugelVoteResultDTO(gaugeVote));
    }

    @PostMapping("/{post-id}/cardVote")
    @Operation(summary = "카드 투표 API", description = "카드 투표에 참여합니다.")
    @Parameters({
            @Parameter(name = "selectList", description = "<Integer> 투표 후보 id 리스트"),
            @Parameter(name = "atk", description = "RequestHeader - 로그인한 사용자의 accessToken")
    })
    public ApiResponse<VoteResponseDTO.CardVoteResponseDTO> join(@RequestBody @Valid VoteRequestDTO.CardVoteRequestDTO request,
                                                                 @PathVariable(name = "post-id") Long PostId,
                                                                 @RequestHeader("atk") String atk,
                                                                 HttpServletRequest request2) {
        Long UserId = jwtTokenService.JwtToId(request2);
        Card_vote cardVote = voteService.castCardVote(request, PostId, UserId);
        return ApiResponse.onSuccess(VoteConverter.toAddCardVoteResultDTO(cardVote));
    }
}

