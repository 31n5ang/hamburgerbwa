package kr.hamburgersee.domain.review;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ReviewCardDto {
    private Long reviewId;
    private String shopName;
    private String title;
    private String omittedContent;
    private String username;
    private String thumbnailUrl;
    private LocalDateTime createdDate;
    private List<ReviewTagType> tagTypes;
    private String region;
}
