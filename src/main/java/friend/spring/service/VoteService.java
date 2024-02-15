package friend.spring.service;

import friend.spring.domain.Card_vote;
import friend.spring.domain.Gauge_vote;
import friend.spring.domain.General_vote;
import friend.spring.web.dto.VoteRequestDTO;

public interface VoteService {

    General_vote castGeneralVote(VoteRequestDTO.GeneralVoteRequestDTO request,Long PostId, Long userId);
    Gauge_vote castGaugeVote(VoteRequestDTO.GaugeVoteRequestDTO request,Long PostId, Long userId);

    Card_vote castCardVote(VoteRequestDTO.CardVoteRequestDTO request, Long PostId, Long userId);

}
