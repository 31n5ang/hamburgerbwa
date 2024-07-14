package kr.hamburgersee.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.hamburgersee.domain.common.RegionType;
import lombok.Data;

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

    // 생성자
    private MemberJoinForm(RegionType region, GenderType gender) {
        this.region = region;
        this.gender = gender;
    }

    // 팩토리 메소드
    // 성별과 지역의 기본값을 설정한 빈 객체를 생성하는 메소드이다.
    public static MemberJoinForm createNewDefaultForm() {
        return new MemberJoinForm(RegionType.ETC, GenderType.NONE);
    }
}