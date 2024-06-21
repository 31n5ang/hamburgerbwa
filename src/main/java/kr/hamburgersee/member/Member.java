package kr.hamburgersee.member;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String password;

    private String email;

    private String nickname;

    private String region;

    @CreationTimestamp
    private LocalDateTime joinedDate;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
}
