package friend.spring.web.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

public class VoteRequestDTO {
    @Getter
    public static class GeneralVoteRequestDTO{
        List<Long> selectList;
    }

    @Getter
    public static class GaugeVoteRequestDTO{
        Integer value;
    }

    @Getter
    public static class CardVoteRequestDTO{
        List<Long> selectList;
    }
}
