package kr.hamburgersee.dto.board;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardWriteForm {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
