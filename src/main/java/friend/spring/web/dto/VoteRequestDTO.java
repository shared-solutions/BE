package friend.spring.web.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

public class VoteRequestDTO {
    @Getter
    public static class GeneralVoteRequestDTO{
        Long postId;
        List<Long> selectList;
    }

    @Getter
    public static class GaugeVoteRequestDTO{
        Long postId;
        Integer value;
    }

}
