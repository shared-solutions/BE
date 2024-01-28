package friend.spring.web.dto;

import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequestDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserJoinRequest{//회원가입 요청
        String email;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserLoginRequest{//로그인 요청
        String email;
        String password;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class EmailSendRequest{//이메일 전송 요청
        String email;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class EmailDupCheckRequest { // 이메일 중복 확인
        String email;
        boolean isForJoin;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserDeleteReq { // 회원 탈퇴 요청
        Long userIdx;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class EmailCodeCheckRequest{
        String email;
        String authCode;
    }
}
