package kr.hamburgersee.domain.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSessionInfo {
    private Long memberId;
    private String username;
}
