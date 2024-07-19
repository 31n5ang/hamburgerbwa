package kr.hamburgersee.domain.review;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String shopName;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<ReviewTagType> tagTypes = new ArrayList<>();

    private Long memberId;

    private List<String> allImageUrls;
}
