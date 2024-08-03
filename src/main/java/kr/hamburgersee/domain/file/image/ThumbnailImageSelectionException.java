package kr.hamburgersee.domain.file.image;

public class ThumbnailImageSelectionException extends RuntimeException {
    public ThumbnailImageSelectionException() {
    }

    public ThumbnailImageSelectionException(String message) {
        super(message);
    }

    public ThumbnailImageSelectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThumbnailImageSelectionException(Throwable cause) {
        super(cause);
    }

    public ThumbnailImageSelectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
