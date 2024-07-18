package kr.hamburgersee.domain.file.image;

public class ReviewImageUploadFailException extends RuntimeException{
    public ReviewImageUploadFailException() {
    }

    public ReviewImageUploadFailException(String message) {
        super(message);
    }

    public ReviewImageUploadFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewImageUploadFailException(Throwable cause) {
        super(cause);
    }
}
