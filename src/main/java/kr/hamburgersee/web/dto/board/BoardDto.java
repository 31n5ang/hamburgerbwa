package kr.hamburgersee.web.dto.board;

import jakarta.validation.constraints.NotNull;
import kr.hamburgersee.domain.base.At;
import kr.hamburgersee.domain.base.Likable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    private Long boardId;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private Likable likable;
    private At at;

    private Long memberId;
    private String nickname;
}
