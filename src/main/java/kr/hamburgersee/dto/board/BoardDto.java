package kr.hamburgersee.dto.board;

import jakarta.validation.constraints.NotNull;
import kr.hamburgersee.domain.Board;
import kr.hamburgersee.domain.BoardStatus;
import kr.hamburgersee.domain.Member;
import kr.hamburgersee.domain.base.At;
import kr.hamburgersee.domain.base.Likable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private Long boardId;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private Likable likable;
    private At at;
    private BoardStatus status;

    private Long memberId;
    private String nickname;
}
