package kr.hamburgersee.domain.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewSearchDto {
    private String keyword;
    private boolean isKeywordInTitle;
    private boolean isKeywordInShopName;
    private boolean isKeywordInContent;
}
