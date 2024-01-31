package friend.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class TokenDTO {

    //1. 로그인 시 토큰을 응답
    private String types;  // atk, rtk
    private String token; // jwt 토큰
    private Date tokenExpriresTime; // 토큰 만료시간
}