package kr.hamburgersee.domain.file;

import org.junit.jupiter.api.Test;

class FileUtilsTest {
    @Test
    void exampleFileUtils() {
        String name1 = FileUtils.generateUploadFilename("png");
        String name2 = FileUtils.generateUploadFilename(".png");
        String name3 = FileUtils.generateUploadFilename("");
        String ext = FileUtils.parseExt("test.png");
        String pureExt = FileUtils.parsePureExt("test.png");
        System.out.println("name1 = " + name1);
        System.out.println("name2 = " + name2);
        System.out.println("name3 = " + name3);
        System.out.println("ext = " + ext);
        System.out.println("pureExt = " + pureExt);
    }
}
