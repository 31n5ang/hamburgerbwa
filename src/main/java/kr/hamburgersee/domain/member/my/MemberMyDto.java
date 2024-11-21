package kr.hamburgersee.domain.member.my;

import kr.hamburgersee.domain.common.RegionType;
import kr.hamburgersee.domain.member.GenderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberMyDto {
    // id, nickname, memberProfileUrl은 MemberSessionInfo로 제공할 수 있습니다.
    private RegionType region;

    private GenderType gender;

    private String bio;

    private Long likedCount;

    private Long reviewCount;

    private Long commentCount;
}
