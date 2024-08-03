package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.aws.storage.StorageUploadFileType;
import org.springframework.stereotype.Component;

@Component
public interface ImageManager {
    public String uploadImage(byte[] file, String originalFilename, StorageUploadFileType storageUploadFileType);

    public void deleteImage(String url, StorageUploadFileType storageUploadFileType);
}
