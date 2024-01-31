package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class VoteResponseDTO {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneralVoteResponseDTO {
        Long generalVoteId;
        LocalDateTime createdAt;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GaugeVoteResponseDTO {
        Long gaugeVoteId;
        LocalDateTime createdAt;
    }
}
