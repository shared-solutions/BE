package friend.spring.converter;

import friend.spring.domain.Candidate;
import friend.spring.web.dto.CandidateResponseDTO;

public class CandidateConverter {
    public static CandidateResponseDTO.AddCandidateResultDTO toAddCandidateResultDTO(Candidate candidate) {
        return CandidateResponseDTO.AddCandidateResultDTO.builder()
                .candidateId(candidate.getId())
                .createdAt(candidate.getCreatedAt())
                .build();
    }
}
