package kr.hamburgersee.domain.entity.image;

import jakarta.persistence.*;
import kr.hamburgersee.domain.At;

@MappedSuperclass
public abstract class Image {
    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String filename;

    @Embedded
    private At at;


}
