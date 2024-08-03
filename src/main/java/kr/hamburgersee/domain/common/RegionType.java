package kr.hamburgersee.domain.common;

public enum RegionType {
    ETC("기타"),
    SEOUL("서울"),
    DAEJEON("대전"),
    CHEONAN("천안"),
    BUSAN("부산");

    public final String displayName;

    RegionType(String displayName) {
        this.displayName = displayName;
    }
}
