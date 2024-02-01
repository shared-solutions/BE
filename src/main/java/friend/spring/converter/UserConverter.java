package friend.spring.converter;

import friend.spring.domain.User;
import friend.spring.web.dto.UserResponseDTO;
import io.swagger.models.auth.In;

public class UserConverter {
    public static UserResponseDTO.MyPageResDTO toMypageResDTO(User user){
        return UserResponseDTO.MyPageResDTO.builder()
                .userPhoto(user.getImage())
                .userName(user.getName())
                .userPoint(user.getPoint())
                .userLevelInt(user.getLevel().getLike())
                .userLevelName(user.getLevel().getName())
                .userRecommend(user.getLike())
                .build();
    }

    public static UserResponseDTO.PointViewDTO toPointViewResDTO(Integer point){
        return UserResponseDTO.PointViewDTO.builder()
                .point(point)
                .build();
    }

}
