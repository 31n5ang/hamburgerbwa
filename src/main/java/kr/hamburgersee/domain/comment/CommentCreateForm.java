package kr.hamburgersee.domain.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class CommentCreateForm {
    @Length(min = 1, max = 500)
    @NotBlank
    private String content;
}
