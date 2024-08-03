package kr.hamburgersee.domain.member;

public class MemberException extends RuntimeException {
    public MemberException() {
        super("Member exception");
    }

    public MemberException(String message) {
        super(message);
    }

    public MemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberException(Throwable cause) {
        super(cause);
    }
}
