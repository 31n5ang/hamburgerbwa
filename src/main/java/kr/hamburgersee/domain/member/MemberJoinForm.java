package kr.hamburgersee.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.hamburgersee.domain.common.RegionType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MemberJoinForm {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String rawPassword;

    @NotBlank
    private String nickname;

    private RegionType region;

    private GenderType gender;

    private String bio;

    private MultipartFile profileImageBase64;

    // 생성자
    private MemberJoinForm(RegionType region, GenderType gender) {
        this.region = region;
        this.gender = gender;
    }

    // 팩토리 메소드
    public static MemberJoinForm createDefaultEmpty() {
        return new MemberJoinForm(RegionType.ETC, GenderType.NONE);
    }
}
