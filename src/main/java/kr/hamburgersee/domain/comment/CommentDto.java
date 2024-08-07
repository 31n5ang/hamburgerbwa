package kr.hamburgersee.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private String content;

    private int good;

    private LocalDateTime createdDate;

    private String ago;

    // member
    private String nickname;

    private String profileUrl;
}
