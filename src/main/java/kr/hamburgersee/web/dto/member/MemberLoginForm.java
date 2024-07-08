package kr.hamburgersee.web.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MemberLoginForm {
    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;
}
