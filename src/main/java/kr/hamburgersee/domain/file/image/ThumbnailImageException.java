package kr.hamburgersee.domain.file.image;

public class ThumbnailImageException extends RuntimeException {
    public ThumbnailImageException() {
    }

    public ThumbnailImageException(String message) {
        super(message);
    }

    public ThumbnailImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThumbnailImageException(Throwable cause) {
        super(cause);
    }

    public ThumbnailImageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
