package kr.hamburgersee.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberSessionInfo {
    private Long memberId;
    private String email;
}
