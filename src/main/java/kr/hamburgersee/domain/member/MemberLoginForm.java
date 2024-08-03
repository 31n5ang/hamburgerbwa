package kr.hamburgersee.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginForm {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String rawPassword;
}
