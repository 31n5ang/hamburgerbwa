package kr.hamburgersee.base;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntity {
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
