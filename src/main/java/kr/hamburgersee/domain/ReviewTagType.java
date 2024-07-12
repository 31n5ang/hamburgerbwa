package kr.hamburgersee.domain;

public enum ReviewTagType {
    BEEF("소고기"),
    CHICKEN("닭고기"),
    PORK("돼지고기"),
    VEGAN("비건"),
    LIGHT("다이어트"),
    HEAVY("벌크업");

    public final String displayName;

    ReviewTagType(String displayName) {
        this.displayName = displayName;
    }
}
