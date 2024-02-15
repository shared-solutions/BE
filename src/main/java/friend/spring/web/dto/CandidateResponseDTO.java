package friend.spring.web.dto;

import lombok.*;

import java.time.LocalDateTime;

public class CandidateResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CandidateSummaryRes {
        Long candidate_id; // 후보 아이디
        String candidate_name; // 후보 이름
        String candidate_image; // 후보 이미지
        Double ratio; // 비율
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCandidateResultDTO {
        Long candidateId;
        LocalDateTime createdAt;
    }
}
