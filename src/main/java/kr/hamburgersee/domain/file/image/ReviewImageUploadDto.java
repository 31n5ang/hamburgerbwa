package kr.hamburgersee.domain.file.image;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ReviewImageUploadDto {
    String filename;
    String base64;
}
