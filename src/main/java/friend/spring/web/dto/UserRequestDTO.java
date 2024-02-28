package friend.spring.web.dto;

import friend.spring.domain.enums.Gender;
import io.swagger.models.auth.In;
import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

public class UserRequestDTO {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserJoinRequest {

        String email;
        String password;
        String nickname;
        String phone;
        Integer gender;
        boolean agree_info;
        boolean agree_marketing;
        LocalDate birth;
        boolean is_deleted;
        Integer point;
        String kakao;
        Integer like;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserLoginRequest {//로그인 요청
        String email;
        String password;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class EmailCheckReq {

        @Email
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;

        @NotEmpty(message = "인증 번호를 입력해 주세요")
        private String authNum;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class NicknameCheckReq {
        private String nickname;
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
    public static class EmailSendReq {
        @Email//1)@기호를 포함해야 한다.2_@기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인 파트가 존재해야 한다.3)도메인 파트는 최소하나의 점과
        //그 뒤에 최소한 2개의 알파벳을 가진다를 검증
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class EmailSendCheckReq {
        @Email//1)@기호를 포함해야 한다.2_@기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인 파트가 존재해야 한다.3)도메인 파트는 최소하나의 점과
        //그 뒤에 최소한 2개의 알파벳을 가진다를 검증
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;
        private String authNum;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PasswordEmailSendReq {
        @Email//1)@기호를 포함해야 한다.2_@기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인 파트가 존재해야 한다.3)도메인 파트는 최소하나의 점과
        //그 뒤에 최소한 2개의 알파벳을 가진다를 검증
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;
        private String name;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PasswordEmailSendCheckReq {
        @Email//1)@기호를 포함해야 한다.2_@기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인 파트가 존재해야 한다.3)도메인 파트는 최소하나의 점과
        //그 뒤에 최소한 2개의 알파벳을 가진다를 검증
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;
        private String name;
        private String authNum;
    }

    //비밀번호 변경
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PasswordUpdateReq {

        @NotEmpty(message = "변경할 비밀번호를 입력해 주세요")
        private String newPassword;
        private String newPasswordCheck;
    }
}