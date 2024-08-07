package kr.hamburgersee.domain.file.image;

public class ProfileImageUploadFailException extends ProfileImageException {
    public ProfileImageUploadFailException() {
    }

    public ProfileImageUploadFailException(String message) {
        super(message);
    }

    public ProfileImageUploadFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileImageUploadFailException(Throwable cause) {
        super(cause);
    }

    public ProfileImageUploadFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
