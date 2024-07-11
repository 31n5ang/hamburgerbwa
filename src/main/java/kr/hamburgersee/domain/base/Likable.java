package kr.hamburgersee.domain.base;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likable {
    private Integer likes;
    private Integer dislikes;

    public static Likable defaultValue() {
        return new Likable(0, 0);
    }
}
