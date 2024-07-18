package kr.hamburgersee.domain.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import kr.hamburgersee.domain.file.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client s3Client;

    @Override
    public String upload(byte[] file, String uploadFilename) {
        String pureExt = FileUtils.parsePureExt(uploadFilename);
        ObjectMetadata metadata = createDefaultObjectMetadata(file, "image/" + pureExt);
        s3Client.putObject(bucket, uploadFilename, new ByteArrayInputStream(file), metadata);
        return getUploadedUrl(uploadFilename);
    }

    @Override
    public void delete(String uploadedUrl) {
        String uploadedFilename = parseUploadedFilename(uploadedUrl);
        s3Client.deleteObject(bucket, uploadedFilename);
    }

    @Override
    public String getUploadedUrl(String uploadedFilename) {
        return s3Client.getUrl(bucket, uploadedFilename).toString();
    }

    private static String parseUploadedFilename(String uploadedUrl) {
        return uploadedUrl.substring(uploadedUrl.lastIndexOf('/') + 1);
    }

    private static ObjectMetadata createDefaultObjectMetadata(byte[] file, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentEncoding("UTF-8");
        metadata.setContentLength(file.length);
        metadata.setContentType(contentType);
        return metadata;
    }
}
