package kr.hamburgersee.domain.entity;

import jakarta.persistence.*;
import kr.hamburgersee.domain.At;
import kr.hamburgersee.domain.CommentStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @Embedded
    private At at;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @ColumnDefault("default 0")
    private int good;


}
