package kr.hamburgersee.domain.file.image;

import jakarta.persistence.*;
import kr.hamburgersee.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage extends Image {
    @Id @GeneratedValue
    @Column(name = "profile_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
