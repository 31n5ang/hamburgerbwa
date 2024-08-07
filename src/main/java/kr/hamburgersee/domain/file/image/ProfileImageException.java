package kr.hamburgersee.domain.file.image;

public class ProfileImageException extends RuntimeException {
    public ProfileImageException() {
    }

    public ProfileImageException(String message) {
        super(message);
    }

    public ProfileImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileImageException(Throwable cause) {
        super(cause);
    }

    public ProfileImageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
