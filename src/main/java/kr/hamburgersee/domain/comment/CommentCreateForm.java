package kr.hamburgersee.domain.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreateForm {
    @NotBlank
    private String content;
}
