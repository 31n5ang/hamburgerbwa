package kr.hamburgersee.domain.session;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSessionInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long memberId;
    private String nickname;
    private String memberProfileUrl;
}
