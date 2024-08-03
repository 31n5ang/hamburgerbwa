package kr.hamburgersee.domain.review;

public class ReviewException extends RuntimeException{
    public ReviewException() {
    }

    public ReviewException(String message) {
        super(message);
    }

    public ReviewException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewException(Throwable cause) {
        super(cause);
    }

    public ReviewException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
