package friend.spring.web.dto;

import friend.spring.domain.enums.InquiryCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class MyPageRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileEditNameReq {
        @Email//1)@기호를 포함해야 한다.2_@기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인 파트가 존재해야 한다.3)도메인 파트는 최소하나의 점과
        //그 뒤에 최소한 2개의 알파벳을 가진다를 검증
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;
        private String certification;
        String nickName;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileEditEmailReq {
        @Email//1)@기호를 포함해야 한다.2_@기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인 파트가 존재해야 한다.3)도메인 파트는 최소하나의 점과
        //그 뒤에 최소한 2개의 알파벳을 가진다를 검증
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String curEmail;
        private String certification;
        String changeEmail;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileEditPhoneReq {
        @Email//1)@기호를 포함해야 한다.2_@기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인 파트가 존재해야 한다.3)도메인 파트는 최소하나의 점과
        //그 뒤에 최소한 2개의 알파벳을 가진다를 검증
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String email;
        private String certification;
        String phone;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileEditPasswordReq {
        String curPassword;
        String changePassword;
        String checkPassword;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileEditSecurityReq {
        @Email//1)@기호를 포함해야 한다.2_@기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인 파트가 존재해야 한다.3)도메인 파트는 최소하나의 점과
        //그 뒤에 최소한 2개의 알파벳을 가진다를 검증
        @NotEmpty(message = "이메일을 입력해 주세요")
        private String curEmail;
        private String certification;
        @Email
        @NotEmpty(message = "이메일을 입력해 주세요")
        String changeEmail;
        String nxtCertification;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyInquiryReq {
        InquiryCategory inquiryCategory;
        String content;
    }
}