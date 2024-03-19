package friend.spring.converter;

import friend.spring.domain.Card_vote;
import friend.spring.domain.Gauge_vote;
import friend.spring.domain.General_vote;
import friend.spring.domain.Post;
import friend.spring.domain.enums.PostState;
import friend.spring.domain.enums.PostType;
import friend.spring.domain.enums.PostVoteType;
import friend.spring.web.dto.PostRequestDTO;
import friend.spring.web.dto.PostResponseDTO;
import friend.spring.web.dto.VoteRequestDTO;
import friend.spring.web.dto.VoteResponseDTO;

import java.time.LocalDateTime;

import static friend.spring.domain.enums.PostType.VOTE;

public class VoteConverter {
    public static VoteResponseDTO.GeneralVoteResponseDTO toAddGeneralVoteResultDTO(General_vote generalVote) {
        return VoteResponseDTO.GeneralVoteResponseDTO.builder()
                .generalVoteId(generalVote.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static VoteResponseDTO.GaugeVoteResponseDTO toAddGaugelVoteResultDTO(Gauge_vote gaugeVote) {
        return VoteResponseDTO.GaugeVoteResponseDTO.builder()
                .gaugeVoteId(gaugeVote.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static VoteResponseDTO.CardVoteResponseDTO toAddCardVoteResultDTO(Card_vote cardVote) {
        return VoteResponseDTO.CardVoteResponseDTO.builder()
                .cardVoteId(cardVote.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static General_vote toGeneralVote(VoteRequestDTO.GeneralVoteRequestDTO request) {

        return General_vote.builder()
                .build();
    }

    public static Gauge_vote toGaugeVote(VoteRequestDTO.GaugeVoteRequestDTO request) {

        return Gauge_vote.builder()
                .value(request.getValue())
                .build();
    }

    public static Card_vote toCardVote(VoteRequestDTO.CardVoteRequestDTO request) {

        return Card_vote.builder()
                .build();
    }
}
