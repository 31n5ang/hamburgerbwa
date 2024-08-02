package kr.hamburgersee.domain.file.image;

import jakarta.persistence.*;
import kr.hamburgersee.domain.common.At;
import kr.hamburgersee.domain.common.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Image extends Date {
    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String filename;

    // 생성자
    protected Image(String uploadedUrl, String filename) {
        this.url = uploadedUrl;
        this.filename = filename;
    }
}
