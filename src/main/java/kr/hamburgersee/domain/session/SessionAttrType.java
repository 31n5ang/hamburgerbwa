package kr.hamburgersee.domain.session;

public enum SessionAttrType {
    MEMBER_SESSION_INFO("memberSessionInfo"),
    REQUEST_REDIRECT_URI("requestRedirectUri");

    public final String attribute;

    SessionAttrType(String attribute) {
        this.attribute = attribute;
    }
}
