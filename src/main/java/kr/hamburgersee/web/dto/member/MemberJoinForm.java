package kr.hamburgersee.web.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import kr.hamburgersee.domain.DomainConst;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class MemberJoinForm {
    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String nickname;

    @Length(max = DomainConst.MEMBER_INTRO_MAX)
    private String intro;
}
