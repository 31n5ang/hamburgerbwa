package kr.hamburgersee.dto.comment;

import kr.hamburgersee.domain.base.At;
import kr.hamburgersee.domain.base.Likable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long memberId;
    private Long boardId;
    private String content;
    private String memberNickname;
    private Likable likable;
    private At at;

    public CommentDto(Long memberId, Long boardId, String content, String memberNickname) {
        this.memberId = memberId;
        this.boardId = boardId;
        this.content = content;
        this.memberNickname = memberNickname;
    }
}
