package kr.hamburgersee.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.hamburgersee.domain.DomainConst;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberJoinForm {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @Length(max = DomainConst.MEMBER_INTRO_MAX)
    private String intro;
}
