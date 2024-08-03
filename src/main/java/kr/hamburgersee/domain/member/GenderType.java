package kr.hamburgersee.domain.member;

public enum GenderType {
    MALE("남성"),
    FEMALE("여성"),
    NONE("선택 안함");

    public final String displayName;

    GenderType(String displayName) {
        this.displayName = displayName;
    }
}
