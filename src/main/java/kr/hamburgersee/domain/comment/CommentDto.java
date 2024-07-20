package kr.hamburgersee.domain.comment;

import kr.hamburgersee.domain.common.At;
import kr.hamburgersee.domain.file.image.ProfileImage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    private String content;

    private int good;

    private At at;

    // member
    private String nickname;

    private String profileImageUploadedUrl;
}
