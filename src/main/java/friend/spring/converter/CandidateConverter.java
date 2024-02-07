package friend.spring.converter;

import friend.spring.domain.Candidate;
import friend.spring.web.dto.CandidateResponseDTO;

public class CandidateConverter {

    public static CandidateResponseDTO.CandidateSummaryRes toCandidateSummaryRes(Candidate candidate, Double percent) {
        return CandidateResponseDTO.CandidateSummaryRes.builder()
                .candidate_id(candidate.getId())
                .candidate_name(candidate.getName())
                .ratio(percent)
                .build();
    }
}
