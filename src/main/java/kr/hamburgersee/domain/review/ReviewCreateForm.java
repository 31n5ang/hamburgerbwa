package kr.hamburgersee.domain.review;

import kr.hamburgersee.domain.common.RegionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateForm {
    private RegionType region;
    private String shopName;
    private String title;
    private String content;
    private List<ReviewTagType> tagTypes = new ArrayList<>();
    private Long memberId;
}
