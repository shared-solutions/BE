package friend.spring.domain;

import friend.spring.domain.common.BaseEntity;
import friend.spring.domain.enums.Gender;
import friend.spring.domain.enums.RoleType;
import friend.spring.domain.mapping.Comment_like;
import friend.spring.domain.mapping.Post_like;
import friend.spring.domain.mapping.Post_scrap;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 68)
    private String password;

    @Email////1)@기호를 포함해야 한다.2_@기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인 파트가 존재해야 한다.3)도메인 파트는 최소하나의 점과
    //그 뒤에 최소한 2개의 알파벳을 가진다를 검증
    @Column(nullable = false, length = 50)
    private String email;

    @Pattern(regexp = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$", message = "전화번호 형식이 맞지 않습니다.")
    @Column(nullable = false, length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private Gender gender;

    @Column(nullable = false, length = 30)
    private String nickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = true)
    private Boolean agree_marketing;

    @Column(nullable = true)
    private Boolean agree_info;

    @Column(nullable = true)//잠시 true로 수정
    private Boolean is_deleted;

    @Column(nullable = true)
    @Builder.Default
    private Integer point=0;

    @Column(nullable = true)
    private String kakao;

    @Column(nullable = true)//잠시 true
    private Integer like;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private Level level;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Point> pointList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alarm> alarmList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Report> reportList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post_like> postLikeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post_scrap> postScrapList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Comment_like> commentLikeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<General_vote> generalVoteList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Gauge_vote> gaugeVoteList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Inquiry> InquiryList = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    @JoinColumn(name = "file_id")
    private File file;

    // UserDetails 상속
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Card_vote> cardVoteList = new ArrayList<>();

    public void setPoint(Integer point) {
        this.point = point;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setNickname(String nickname){ this.nickname = nickname; }

    public void setEmail(String email){this.email = email;}

    public void setPhone(String  phone){this.phone = phone;}

    public void setPassword(String password){this.password = password;}
}

