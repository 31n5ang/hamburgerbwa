package kr.hamburgersee.domain.aws;

public interface StorageService {
    String upload(byte[] file, String uploadFilename);

    void delete(String uploadedUrl);

    String getUploadedUrl(String uploadedFilename);
}
