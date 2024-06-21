package kr.hamburgersee.exception;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException() {
        super("해당 Member를 조회할 수 없습니다.");
    }
    public MemberNotFoundException(String message) {
        super(message);
    }
}
