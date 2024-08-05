package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.aws.storage.StorageService;
import kr.hamburgersee.domain.aws.storage.StorageUploadFileType;
import kr.hamburgersee.domain.file.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3StorageImageManager implements ImageManager {
    private final StorageService storageService;

    public String uploadImage(byte[] file, String originalFilename, StorageUploadFileType storageUploadFileType) {
        String ext = FileUtils.parseExt(originalFilename);
        String uploadFilename = FileUtils.generateUploadFilename(ext);
        return storageService.upload(file, uploadFilename, storageUploadFileType);
    }

    public void deleteImage(String url, StorageUploadFileType storageUploadFileType) {
        storageService.delete(url, storageUploadFileType);
    }
}
