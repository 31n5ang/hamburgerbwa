package kr.hamburgersee.domain.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberAuthenticatedInfo {
    private Long memberId;
    private String nickname;
    private String memberProfileUrl;
}
