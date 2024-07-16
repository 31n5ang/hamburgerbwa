package kr.hamburgersee.domain.member;

public class MemberDuplicateEmailException extends MemberException {
    MemberDuplicateEmailException() {
        super("Email이 중복됩니다.");
    }
    public MemberDuplicateEmailException(String message) {
        super(message);
    }

    public MemberDuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberDuplicateEmailException(Throwable cause) {
        super(cause);
    }
}
