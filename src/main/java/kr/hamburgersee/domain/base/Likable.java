package kr.hamburgersee.domain.base;

import jakarta.persistence.Embeddable;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
public class Likable {
    @ColumnDefault("0")
    private int likes;

    @ColumnDefault("0")
    private int dislikes;
}
