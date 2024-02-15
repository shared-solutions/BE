package friend.spring.converter;

import friend.spring.domain.Candidate;
import friend.spring.web.dto.CandidateResponseDTO;

public class CandidateConverter {

    public static CandidateResponseDTO.CandidateSummaryRes toCandidateSummaryRes(Candidate candidate, Double percent) {
        String candidate_image = null;
        if (candidate.getFile() != null) {
            candidate_image = candidate.getFile().getUrl();
        }

        return CandidateResponseDTO.CandidateSummaryRes.builder()
                .candidate_id(candidate.getId())
                .candidate_name(candidate.getName())
                .candidate_image(candidate_image)
                .ratio(percent)
                .build();
    }

    public static CandidateResponseDTO.AddCandidateResultDTO toAddCandidateResultDTO(Candidate candidate) {
        return CandidateResponseDTO.AddCandidateResultDTO.builder()
                .candidateId(candidate.getId())
                .createdAt(candidate.getCreatedAt())
                .build();
    }
}
