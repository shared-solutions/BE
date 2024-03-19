package friend.spring.web.dto;

import lombok.*;

public class CandidateRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddCandidateRequestDTO {
        String optionString;
        String optionImg;
    }
}
