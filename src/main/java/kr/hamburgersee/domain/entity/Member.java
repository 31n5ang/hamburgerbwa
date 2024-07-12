package kr.hamburgersee.domain.entity;

import jakarta.persistence.*;
import kr.hamburgersee.domain.At;
import kr.hamburgersee.domain.GenderType;
import kr.hamburgersee.domain.RegionType;
import kr.hamburgersee.domain.RolesType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String encPassword;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private RegionType region;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    private RolesType roles;

    @Lob
    private String bio;

    @Embedded
    private At at;

    // 생성자
    private Member(String email, String encPassword, String nickname, RegionType region, GenderType gender, String bio) {
        this.email = email;
        this.encPassword = encPassword;
        this.nickname = nickname;
        this.region = region;
        this.gender = gender;
        this.bio = bio;
        //
        this.roles = RolesType.USER;
    }

    // 팩토리 메소드
    public static Member createNewMember(String email, String encPassword, String nickname, RegionType region,
                               GenderType gender,
                           String bio) {
        return new Member(email, encPassword, nickname, region, gender, bio);
    }
}
