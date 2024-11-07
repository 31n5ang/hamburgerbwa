package kr.hamburgersee.domain.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FileNameUtilsTest {
    @Test
    @DisplayName("확장자_추출_기능")
    void exampleFileUtils() {
        // Given
        String EXT_PNG = ".png";
        String EXT_JPG = ".jpg";

        String PURE_EXT_PNG = "png";
        String PURE_EXT_JPG = "jpg";

        String sample1 = FileNameUtils.generateUploadFilename(EXT_PNG);
        String sample2 = FileNameUtils.generateUploadFilename(EXT_JPG);
        String sample3 = FileNameUtils.generateUploadFilename(PURE_EXT_PNG);
        String sample4 = FileNameUtils.generateUploadFilename(PURE_EXT_JPG);

        // When
        String extFromSample1 = FileNameUtils.parseExt(sample1);
        String extFromSample2 = FileNameUtils.parseExt(sample2);
        String extFromSample3 = FileNameUtils.parseExt(sample3);
        String extFromSample4 = FileNameUtils.parseExt(sample4);

        String pureExtFromSample1 = FileNameUtils.parsePureExt(sample1);
        String pureExtFromSample2 = FileNameUtils.parsePureExt(sample2);
        String pureExtFromSample3 = FileNameUtils.parsePureExt(sample3);
        String pureExtFromSample4 = FileNameUtils.parsePureExt(sample4);

        // Then
        assertThat(extFromSample1).isEqualTo(extFromSample3);
        assertThat(extFromSample2).isEqualTo(extFromSample4);
        assertThat(pureExtFromSample1).isEqualTo(pureExtFromSample3);
        assertThat(pureExtFromSample2).isEqualTo(pureExtFromSample4);
    }
}
