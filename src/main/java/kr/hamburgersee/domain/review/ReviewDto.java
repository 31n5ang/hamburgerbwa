package kr.hamburgersee.domain.review;

import kr.hamburgersee.domain.common.RegionType;
import kr.hamburgersee.domain.common.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ReviewDto {
    private String title;

    private RegionType region;

    private String shopName;

    private String content;

    private List<ReviewTagType> tags = new ArrayList<>();

    private Date date;

    private int good;

    private String nickname;

    private Long memberId;
}
