package friend.spring.converter;

import friend.spring.domain.User;
import friend.spring.web.dto.UserResponseDTO;

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


    public static UserResponseDTO.EmailSendRes toEmailSendRes(String code) {
        return UserResponseDTO.EmailSendRes.builder()
                .code(code)
                .build();
    }
}
