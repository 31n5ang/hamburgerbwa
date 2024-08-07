package kr.hamburgersee.domain.aws.storage;

public enum StorageUploadFileType {
    REVIEW_IMAGE("image/review/"),
    REVIEW_THUMBNAIL_IMAGE("image/review/thumbnail/"),
    MEMBER_PROFILE_IMAGE("image/member/");

    public String path;

    StorageUploadFileType(String path) {
        this.path = path;
    }
}
