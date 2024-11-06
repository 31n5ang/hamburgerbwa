package kr.hamburgersee.domain.file;

import org.springframework.util.StringUtils;

import java.util.UUID;

public final class FileNameUtils {
    private FileNameUtils() {}

    public static String generateUploadFilename(String ext) {
        StringBuilder uploadFilename = new StringBuilder();
        uploadFilename.append(generateUuid());
        uploadFilename.append("_");
        uploadFilename.append(System.currentTimeMillis());
        if (StringUtils.hasText(ext)) {
            if (ext.charAt(0) == '.') {
                uploadFilename.append(ext);
            } else {
                uploadFilename.append(".");
                uploadFilename.append(ext);
            }
        }
        return uploadFilename.toString();
    }

    public static String parseExt(String filename) {
        int extIndex = filename.lastIndexOf('.');
        if (extIndex == -1) {
            return "";
        } else {
            return filename.substring(extIndex);
        }
    }

    public static String parsePureExt(String filename) {
        int extIndex = filename.lastIndexOf('.');
        if (extIndex == -1) {
            return "";
        } else {
            return filename.substring(extIndex + 1);
        }
    }

    private static String generateUuid() {
        return UUID.randomUUID().toString();
    }
}
