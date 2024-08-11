package kr.hamburgersee.domain.likes;

import jakarta.persistence.*;
import kr.hamburgersee.domain.common.Date;
import lombok.Getter;

@Entity
@Table(name = "likes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
public abstract class Like extends Date {
    @Id @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    // 편의 메소드
    public void updateState(LikeStatus status) {
        this.status = status;
    }
}
