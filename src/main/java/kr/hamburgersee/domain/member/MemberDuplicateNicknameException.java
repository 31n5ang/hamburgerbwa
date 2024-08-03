package kr.hamburgersee.domain.member;

public class MemberDuplicateNicknameException extends MemberException {
    public MemberDuplicateNicknameException() {
        super("Nickname이 중복됩니다.");
    }

    public MemberDuplicateNicknameException(String message) {
        super(message);
    }

    public MemberDuplicateNicknameException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberDuplicateNicknameException(Throwable cause) {
        super(cause);
    }
}
