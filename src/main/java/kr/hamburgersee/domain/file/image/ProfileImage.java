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

    // 생성자
    private ProfileImage(String uploadedUrl, String originalFilename) {
        super(uploadedUrl, originalFilename);
    }

    // 팩토리 메소드
    public static ProfileImage create(String uploadedUrl, String originalFilename) {
        return new ProfileImage(uploadedUrl, originalFilename);
    }
}
