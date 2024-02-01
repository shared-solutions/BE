package friend.spring.converter;

import friend.spring.domain.User;
import friend.spring.domain.enums.Gender;
import friend.spring.web.dto.UserRequestDTO;
import friend.spring.web.dto.UserResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserConverter {


    public static UserResponseDTO.MyPageResDTO toMypageResDTO(User user){
        return UserResponseDTO.MyPageResDTO.builder()
                .userPhoto(user.getImage())
                .userName(user.getNickname())
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

    public static UserResponseDTO.JoinResultDTO joinResultDTO(User user){
        return UserResponseDTO.JoinResultDTO.builder()
                .email(user.getEmail())
                .createAt(LocalDate.from(LocalDateTime.now()))
                .build();

    }
    public static User toUser(UserRequestDTO.UserJoinRequest userJoinRequest){

        Gender gender = null;

        switch (userJoinRequest.getGender()){
            case 1:
                gender = Gender.MALE;
                break;
            case 2:
                gender = Gender.FEMALE;
                break;
            case 3:
                gender = Gender.NONE;
                break;
        }
        return User.builder()
                .email(userJoinRequest.getEmail())
                .password(userJoinRequest.getPassword())
                .nickname(userJoinRequest.getNickname())
                .gender(gender)
                .phone(userJoinRequest.getPhone())
                .agree_info(userJoinRequest.isAgree_info())
                .agree_marketing(userJoinRequest.isAgree_marketing())
                .birth(userJoinRequest.getBirth())
                .name(userJoinRequest.getName())
                .kakao(userJoinRequest.getKakao())
                .image(userJoinRequest.getImage())
                .is_deleted(userJoinRequest.is_deleted())
                .point(userJoinRequest.getPoint())
                .like(userJoinRequest.getLike())
                .build();
    }
}
