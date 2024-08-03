package kr.hamburgersee.domain.aws.storage;

public interface StorageService {
    String upload(byte[] file, String uploadFilename, StorageUploadFileType fileType);

    void delete(String uploadedUrl, StorageUploadFileType fileType);

    String getUploadedUrl(String uploadedFilename);
}
