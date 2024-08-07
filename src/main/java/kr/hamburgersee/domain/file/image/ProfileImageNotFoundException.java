package kr.hamburgersee.domain.file.image;

public class ProfileImageNotFoundException extends ProfileImageException {
    public ProfileImageNotFoundException() {
    }

    public ProfileImageNotFoundException(String message) {
        super(message);
    }

    public ProfileImageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileImageNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProfileImageNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
