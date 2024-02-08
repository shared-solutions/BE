package friend.spring.web.dto;

import lombok.*;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private Long imageId;
    private String imageUrl;
}
