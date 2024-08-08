package kr.hamburgersee.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDeleteDto {
    private Long commentId;
    private Long reviewId;
    private Long memberId;
}
