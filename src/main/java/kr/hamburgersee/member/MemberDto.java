package kr.hamburgersee.member;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {
    private String username;

    private String rawPassword;

    private String email;

    private String nickname;

    private String region;
}
