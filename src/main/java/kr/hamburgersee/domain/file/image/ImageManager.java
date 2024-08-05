package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.aws.storage.StorageUploadFileType;

public interface ImageManager {
    String uploadImage(byte[] file, String originalFilename, StorageUploadFileType storageUploadFileType);

    void deleteImage(String url, StorageUploadFileType storageUploadFileType);
}
