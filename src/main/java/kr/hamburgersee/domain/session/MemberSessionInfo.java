package kr.hamburgersee.domain.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class MemberSessionInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long memberId;
    private String nickname;
    private String memberProfileUrl;
}
