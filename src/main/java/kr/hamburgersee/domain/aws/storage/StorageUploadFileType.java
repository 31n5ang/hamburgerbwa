package kr.hamburgersee.domain.aws.storage;

public enum StorageUploadFileType {
    REVIEW_IMAGE("image/review/"),
    THUMBNAIL_REVIEW_IMAGE("image/review/thumbnail/"),
    MEMBER_ICON_IMAGE("image/member/");

    public String path;

    StorageUploadFileType(String path) {
        this.path = path;
    }
}
