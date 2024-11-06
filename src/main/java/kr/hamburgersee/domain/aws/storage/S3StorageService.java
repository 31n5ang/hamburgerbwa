package kr.hamburgersee.domain.aws.storage;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import kr.hamburgersee.domain.file.FileNameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client s3Client;

    @Override
    public String upload(byte[] file, String uploadFilename, StorageUploadFileType fileType) {
        String pureExt = FileNameUtils.parsePureExt(uploadFilename);
        // content-type이 이미지가 아닐 땐..?
        ObjectMetadata metadata = createDefaultObjectMetadata(file, "image/" + pureExt);
        String uploadUrl = fileType.path + uploadFilename;
        s3Client.putObject(bucket, uploadUrl, new ByteArrayInputStream(file), metadata);
        return getUploadedUrl(uploadUrl);
    }

    @Override
    public void delete(String uploadedUrl, StorageUploadFileType fileType) {
        String uploadedFilename = parseUploadedFilename(uploadedUrl);
        s3Client.deleteObject(bucket, fileType.path + uploadedFilename);
    }

    @Override
    public String getUploadedUrl(String uploadUrl) {
        return s3Client.getUrl(bucket, uploadUrl).toString();
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
