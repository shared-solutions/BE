package friend.spring.web.controller;

import friend.spring.apiPayload.ApiResponse;
import friend.spring.converter.VoteConverter;
import friend.spring.domain.Card_vote;
import friend.spring.domain.Gauge_vote;
import friend.spring.domain.General_vote;
import friend.spring.service.VoteService;
import friend.spring.web.dto.VoteRequestDTO;
import friend.spring.web.dto.VoteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/posts")
public class VoteRestController {
    private final VoteService voteService;
    @PostMapping("/{post-id}/{user-id}/generalVote")
    @Operation(summary = "일반 투표 API", description = "임시로 user-id 입력")
    @Parameters({
            @Parameter(name="postId", description="<Long> 글 id"),
            @Parameter(name="selectList", description="<List> 투표 후보 id 리스트")
    })
    public ApiResponse<VoteResponseDTO.GeneralVoteResponseDTO> join(@RequestBody @Valid VoteRequestDTO.GeneralVoteRequestDTO request,
                                                                    @PathVariable(name="user-id")Long UserId){
        General_vote generalVote= voteService.castGeneralVote(request,UserId);
        return ApiResponse.onSuccess(VoteConverter.toAddGeneralVoteResultDTO(generalVote));
    }

    @PostMapping("/{post-id}/{user-id}/gaugeVote")
    @Operation(summary = "게이지 투표 API", description = "임시로 user-id 입력")
    @Parameters({
            @Parameter(name="postId", description="<Long> 글 id"),
            @Parameter(name="selectList", description="<Integer> 게이지")
    })
    public ApiResponse<VoteResponseDTO.GaugeVoteResponseDTO> join(@RequestBody @Valid VoteRequestDTO.GaugeVoteRequestDTO request,
                                                                    @PathVariable(name="user-id")Long UserId){
        Gauge_vote gaugeVote= voteService.castGaugeVote(request,UserId);
        return ApiResponse.onSuccess(VoteConverter.toAddGaugelVoteResultDTO(gaugeVote));
    }

    @PostMapping("/{post-id}/{user-id}/cardVote")
    @Operation(summary = "카드 투표 API", description = "임시로 user-id 입력")
    @Parameters({
            @Parameter(name="postId", description="<Long> 글 id"),
            @Parameter(name="selectList", description="<Integer> 투표 후보 id 리스트")
    })
    public ApiResponse<VoteResponseDTO.CardVoteResponseDTO> join(@RequestBody @Valid VoteRequestDTO.CardVoteRequestDTO request,
                                                                  @PathVariable(name="user-id")Long UserId){
        Card_vote cardVote= voteService.castCardVote(request,UserId);
        return ApiResponse.onSuccess(VoteConverter.toAddCardVoteResultDTO(cardVote));
    }
}

