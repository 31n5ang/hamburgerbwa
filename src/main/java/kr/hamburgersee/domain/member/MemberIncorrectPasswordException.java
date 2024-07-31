package kr.hamburgersee.domain.member;

public class MemberIncorrectPasswordException extends MemberException {
    public MemberIncorrectPasswordException() {
    }

    public MemberIncorrectPasswordException(String message) {
        super(message);
    }

    public MemberIncorrectPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberIncorrectPasswordException(Throwable cause) {
        super(cause);
    }
}
