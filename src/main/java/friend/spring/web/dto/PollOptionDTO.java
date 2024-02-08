package friend.spring.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class PollOptionDTO {

    @Getter
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PollOptionReq {
        private String optionString;
        private MultipartFile optionImg;
    }

    @Getter
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PollOptionRes {
        private String optionString;
        private String optionImgUrl;
    }
}
