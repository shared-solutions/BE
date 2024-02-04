package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CandidateResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CandidateSummaryRes {
        Long candidate_id; // 후보 아이디
        String candidate_name; // 후보 이름
        Double ratio; // 비율
    }
}
