package kr.hamburgersee.domain.comment;

import kr.hamburgersee.domain.common.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    private String content;

    private int good;

    private Date date;

    // member
    private String nickname;

    private String profileImageUploadedUrl;
}
